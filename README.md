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

* add datapack to world

### clicking the block with a stick renamed to the name of the video changes the current playing video to that video, instead of having to change through every video

*this mod is probably the most amount of technical debt I have created*