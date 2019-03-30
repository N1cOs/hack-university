# Copyright 2015 gRPC authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
"""The Python implementation of the GRPC helloworld.Greeter server."""

from concurrent import futures
import time

import grpc

import helloworld_pb2
import helloworld_pb2_grpc

_ONE_DAY_IN_SECONDS = 60 * 60 * 24

musicians = [helloworld_pb2.Musician(name="Face", generes=["gener1", "gener2"], tracks=["zapad", "lol2"] , start_time="12:00", end_time="13:00", x_coord=59.9343, y_coord=30.3361),
            helloworld_pb2.Musician(name="Dog", generes=["gener2", "gener3"], tracks=["zapad", "lol2"], start_time="13:00", end_time="14:00", x_coord=59.9323, y_coord=30.3321),
            helloworld_pb2.Musician(name="Naruto", generes=["gener2", "gener3"], tracks=["zapad", "lol2"], start_time="10:00", end_time="14:00", x_coord=59.9313, y_coord=30.3351),
            helloworld_pb2.Musician(name="You", generes=["gener2", "gener3", "gener2"], tracks=["zapad", "lol2"], start_time="13:00", end_time="14:00", x_coord=59.9323, y_coord=30.3351)]

  
class Greeter(helloworld_pb2_grpc.CommunicatorServicer):

    def Poll(self, request, context): # EmptyMessage
        print("HI")
        for mus in musicians:
            yield mus #returns (stream Musician) {}

    def Send(self, request, context): # Musician
        print("NEW!!!!!!!!")
        musicians.append(request) # returns (EmptyMessage) {}
        return helloworld_pb2.EmptyMessage()

import urllib.request
import instagram
import threading
from instagram.agents import WebAgent
from instagram.entities import Tag
from instagram.entities import Location
from instagram.entities import Media

base_url = "http://instagram.com/p/"
recognition_url = "http://localhost:5000/calculate?url="

agent = WebAgent()
tag = Tag("hearme_musician")
agent.update(tag)

media_list = []
musician_list = []

def check_media(id) -> bool:
    print("OPENING request")
    urll = recognition_url + base_url + id + "/media?size=l"
    print(urll)
    resp = urllib.request.urlopen(urll).read()
    print("request was opended")

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
            agent.update(media)
            agent.update(media.location)
            musicians.append( # returns (EmptyMessage) {}
                helloworld_pb2.Musician(name="None", generes=[""], tracks=[""] , start_time="xx:xx", end_time="xx:xx", x_coord=media.location.coordinates[0], y_coord=media.location.coordinates[1]),
            )
            print("ADDED!")

    return medias[1]


def startService():
    threading.Timer(60.0, startService).start()
    ptr = load_photos(None)
    while ptr is not None:
        ptr = load_photos(ptr)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    helloworld_pb2_grpc.add_CommunicatorServicer_to_server(Greeter(), server)
    server.add_insecure_port('0.0.0.0:50051')
    server.start()
    startService()
    try:
        while True:
            time.sleep(_ONE_DAY_IN_SECONDS)
    except KeyboardInterrupt:
        server.stop(0)


if __name__ == '__main__':
    serve()



