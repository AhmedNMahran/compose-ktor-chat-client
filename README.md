# [Youtube Channel](https://youtube.com/AhmedNMahran) | [Twitter](https://twitter.com/AhmedNMahran)
# compose-ktor-chat-client
This is a sample to demonstrate how to use Compose with Ktor Websockets, did it in this [session](https://www.youtube.com/watch?v=QL3Jk_RZqO8) during MENADD

uses server built in [this project](https://github.com/AhmedNMahran/ktor-socket-server)

# Check the video :

[![DEMO VIDEO](https://img.youtube.com/vi/kU8vZPX_rU8/0.jpg)](https://www.youtube.com/watch?v=kU8vZPX_rU8)

# Release notes:
## version 0.1:
### TODO List:
- todo 1 change screen background
- todo 2 fix message sending logic
- todo 3 make message appear using LazyColumn


## version 0.2:
- finish all todos from v0.1
- a nicer ui
- Separate non-ui logic in a repository
- try making App() composable stateless and depend on repo flows
- fix jdk conflict between android and desktop

## version 0.3:
- fix desktop app not showing chat list
- add kotlinx serialization
- fix message not cleared after sending
- extract MessageComposer composable

## version 0.4:
- add username customization
- add authentication
- fix message composer ui
- change message location for different user
