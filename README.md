# Bobbert

Minecraft 1.7.10 port of [Bobby](https://github.com/Johni0702/bobby), which is a mod that allows for render distances greater than the server's view-distance setting.

It accomplishes this goal by recording and storing (in `.minecraft/.bobby`) all chunks sent by the server which it
then can load and display at a later point when the chunk is outside the server's view-distance.

Largely based on embeddedt's 1.12 port, [Chunkbert](https://github.com/Asek3/chunkbert).
