# THIS IS A BETA VERSION, IT IS UNSTABLE!!!

currently this only works on singleplayer and only if you have ffmpeg on your device,

feel free to send a PR if you want to help!

to add a new video:

* make a new datapack,
* add a json file in the `data/daragetsuvideoplayer/videos` with this structure:

```
{
    "name": "ENTER_NAME",
    "location": "PATH_TO_FILE"
}
```
path example: Downloads/video.mp4 if the video is in your Downloads folder

* if you're on singleplayer, add it to your world as usual, if you're on server, put the data pack on the server world(players do not need to add the data pack or have the video, they only need to have the mod installed)
