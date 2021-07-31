# Usage

The `MLKit.ScanImage` returns a promise.

```javascript
import { NativeModules } from "react-native";
import MLKit from NativeModules;

try{
    const result = await MLKit.ScanImage(filepath);
    console.log("result from the promise:",result);
    setScanned(true);
    navigation.navigate('result',result);
}
catch(error){
    console.log(error);
}
```