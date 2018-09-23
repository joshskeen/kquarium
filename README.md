an ascii aquarium written in kotlin/native
![](http://g.recordit.co/DEBewA7mBi.gif)

```
 _  ______  _    _         _____  _____ _    _ __  __ 
 | |/ / __ \| |  | |  /\   |  __ \|_   _| |  | |  \/  |
 | ' / |  | | |  | | /  \  | |__) | | | | |  | | \  / |
 |  <| |  | | |  | |/ /\ \ |  _  /  | | | |  | | |\/| |
 | . \ |__| | |__| / ____ \| | \ \ _| |_| |__| | |  | |
 |_|\_\___\_\\____/_/    \_\_|  \_\_____|\____/|_|  |_|
 
```

 
##Building

1. compile and install the `ncurses` dependency: 

```
cd ./src/libs/ncurses-5.9/
./configure
./make install
```
2. compile and run `kquarium`

```
./gradlew compileKonanKquarium
./build/konan/bin/<platform name>/kquarium.kexe
```
