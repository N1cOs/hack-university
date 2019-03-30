import urllib.request
import instagram
import threading
from instagram.agents import WebAgent
from instagram.entities import Tag
from instagram.entities import Location
from instagram.entities import Media

base_url = "http://instagram.com/p/"
recognition_url = "http://35.228.95.2:5000/calculate?url="

agent = WebAgent()
tag = Tag("kek")
agent.update(tag)

media_list = []
musician_list = []

def check_media(id) -> bool:
    resp = urllib.request.urlopen(recognition_url + base_url + id).read()

    print(resp)
    if resp is "yes":
        return True
        #musician_list.append(Musician( ... ))


def load_photos(ptr):
    medias = agent.get_media(tag, count=10, pointer=ptr)
    for media in medias[0]:
        if media in media_list:
            break
        media_list.append(media)
        if check_media(media.code):
            push()
    return medias[1]


def startService():
    threading.Timer(60.0, startService).start()
    ptr = load_photos(None)
    while ptr is not None:
        ptr = load_photos(ptr)


startService()
