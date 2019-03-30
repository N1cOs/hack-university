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

musicians = {"Face" : helloworld_pb2.Musician(name="Face", generes=["gener1", "gener2"], tracks=["zapad", "lol2"] , start_time="12:00", end_time="13:00", x_coord=59.9343, y_coord=30.3361),
            "Dog" : helloworld_pb2.Musician(name="Dog", generes=["gener2", "gener3"], tracks=["zapad", "lol2"], start_time="13:00", end_time="14:00", x_coord=59.9323, y_coord=30.3321),
            "Naruto" : helloworld_pb2.Musician(name="Naruto", generes=["gener2", "gener3"], tracks=["zapad", "lol2"], start_time="10:00", end_time="14:00", x_coord=59.9313, y_coord=30.3351),
            "You" : helloworld_pb2.Musician(name="You", generes=["gener2", "gener3", "gener2"], tracks=["zapad", "lol2"], start_time="13:00", end_time="14:00", x_coord=59.9323, y_coord=30.3351)}

  
class Greeter(helloworld_pb2_grpc.CommunicatorServicer):

    i = 0

    def Poll(self, request, context): # EmptyMessage
        print("HI")
        for key, mus in musicians.items():
            yield mus #returns (stream Musician) {}

    def Send(self, request, context): # Musician
        print("LOLOLO!!!!!!!!")
        musicians[request.name] = request # returns (EmptyMessage) {}
        return helloworld_pb2.EmptyMessage()


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    helloworld_pb2_grpc.add_CommunicatorServicer_to_server(Greeter(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    try:
        while True:
            time.sleep(_ONE_DAY_IN_SECONDS)
    except KeyboardInterrupt:
        server.stop(0)


if __name__ == '__main__':
    serve()
