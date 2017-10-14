from flask import Flask, request, redirect, g, render_template, make_response, url_for
from gmusicapi import Mobileclient

import json
import requests
import base64
import urllib

app = Flask(__name__)

#  Client Keys
CLIENT_ID = "1d83da544aac4b9eb89613b05fee1d21"
CLIENT_SECRET = "c49c56beaa3540568a104efe741b8e91"

# Spotify URLS
SPOTIFY_AUTH_URL = "https://accounts.spotify.com/authorize"
SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token"
SPOTIFY_API_BASE_URL = "https://api.spotify.com"
API_VERSION = "v1"
SPOTIFY_API_URL = "{}/{}".format(SPOTIFY_API_BASE_URL, API_VERSION)

# Server-side Parameters
CLIENT_SIDE_URL = "http://45.76.16.65"
PORT = 5000
REDIRECT_URI = "{}:{}/spotifyAuthCallback".format(CLIENT_SIDE_URL, PORT)
SCOPE = "playlist-modify-public playlist-modify-private"
STATE = ""
SHOW_DIALOG_bool = True
SHOW_DIALOG_str = str(SHOW_DIALOG_bool).lower()

auth_query_parameters = {
    "response_type": "code",
    "redirect_uri": REDIRECT_URI,
    "scope": SCOPE,
    "client_id": CLIENT_ID
}

def get_gmusic_playlists(username, password):
    api = Mobileclient()
    print (username + ":" + password)
    logged_in = api.login(username, password, Mobileclient.FROM_MAC_ADDRESS)
    
    if not logged_in:
        print ("Login failed.")

    if api.is_authenticated() :
        playlists = api.get_all_user_playlist_contents()

        output_dict = {}
        for playlist in playlists:
            name = playlist["name"]
            tracks = playlist["tracks"]

            for track in tracks:
                track = track["track"]
                artist = track["artist"]
                title = track["title"]

                if name in output_dict:
                    output_dict[name].append((artist, title))
                else:
                    output_dict[name] = [(artist, title)]
                        
        return output_dict

    return None

def build_header(access_token):
    return {"Authorization":"Bearer {}".format(access_token)}

def get_user_id(access_token):
    header = build_header(access_token)
    get_request = requests.get(SPOTIFY_API_URL + "/me", headers=header)
    return json.loads(get_request.text)["id"]

def create_playlist(access_token, playlist_name, user_id):
    header = build_header(access_token)
    body = {"name" : playlist_name}
    query = "/users/{}/playlists".format(user_id)
    response = requests.post(SPOTIFY_API_URL + query, data=json.dumps(body), headers=header)
    return json.loads(response.text)["id"]

def add_to_playlist(access_token, playlist_id, user_id, tracks):
    if tracks == []:
        return

    header = build_header(access_token)
    body = {"uris": tracks}
    query = "/users/{}/playlists/{}/tracks".format(user_id, playlist_id)
    response = requests.post(SPOTIFY_API_URL + query, data=json.dumps(body), headers=header)

def search_track(access_token, track):
    header = build_header(access_token)
    print track
    query = "/search?q={}&type=track&limit=1".format(urllib.quote(track.encode("utf8")))
    response = requests.get(SPOTIFY_API_URL + query, headers=header)
    tracks = json.loads(response.text)["tracks"]

    if tracks["items"] == []:
        return None
    return tracks["items"][0]["uri"]

@app.route("/spotifyAuthorize")
def authorize():
    url_args = "&".join(["{}={}".format(key,urllib.quote(val)) for key,val in auth_query_parameters.iteritems()])
    auth_url = "{}/?{}".format(SPOTIFY_AUTH_URL, url_args)
    return redirect(auth_url)

@app.route("/spotifyAuthCallback")
def authentication_callback():
    if "user" not in request.cookies or "pass" not in request.cookies:
        return redirect(url_for("index"))

    auth_token = request.args['code']
    code_payload = {
        "grant_type": "authorization_code",
        "code": str(auth_token),
        "redirect_uri": REDIRECT_URI
    }

    base64encoded = base64.b64encode("{}:{}".format(CLIENT_ID, CLIENT_SECRET))
    headers = {"Authorization": "Basic {}".format(base64encoded)}
    post_request = requests.post(SPOTIFY_TOKEN_URL, data=code_payload, headers=headers)

    response_data = json.loads(post_request.text)
    access_token = response_data["access_token"]
    refresh_token = response_data["refresh_token"]
    token_type = response_data["token_type"]
    expires_in = response_data["expires_in"]

    guser = request.cookies.get("user")
    gpass = request.cookies.get("pass")

    playlists = get_gmusic_playlists(guser, gpass)
    if playlists is None:
        return redirect(url_for("index"))

    user_id = get_user_id(access_token)
    for plist_name, tracks in playlists.items():
        plist_id = create_playlist(access_token, plist_name, user_id)
        track_uris = []
        for (artist, track) in tracks:
            trackURI = search_track(access_token, track)
            if trackURI is not None:
                track_uris.append(trackURI)
        add_to_playlist(access_token, plist_id, user_id, track_uris)

    return redirect(url_for("success"))

@app.route("/convert", methods = ["POST"])
def convert_playlist():
    user = request.form['user']
    passwd = request.form['pass']

    resp = make_response(redirect(url_for('authorize')))
    resp.set_cookie("user", user)
    resp.set_cookie("pass", passwd)
    return resp

@app.route("/success")
def success():
    return render_template("index.html", success=True)

@app.route("/")
def index():
    return render_template("index.html", success=False)

if __name__ == "__main__":
    app.run(debug=True, port=PORT)
