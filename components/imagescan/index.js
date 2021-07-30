import React from "react";

import { useState,useEffect,useContext } from "react";

import RecieveSharingIntent from "react-native-receive-sharing-intent";

import { sharedFilesContext } from "../../sharedFilesContext";

import MLKit from "../../MLKitMod";


import{
    View,
    Image,
    Text,
    StyleSheet,
    AppRegistry,
} from "react-native"


const ImageScanStyles = StyleSheet.create({
    container:{
        alignItems:"center",
        justifyContent:"center",
    },
    imgscan:{
        width:200,
        height:200,
        marginTop:50,
    }
});



 


const ImageScan = ({navigation}) => {

    const [Loaded, setLoaded] = useState(false);
    //if image has been scanned
    const [Scanned,setScanned] = useState(false);

    const sharedFiles = useContext(sharedFilesContext);

    

    useEffect(()=>{
        if(sharedFiles){

            let filepath = sharedFiles[0].filePath;
            const onScan = async () => {
                try{
                    const result = await MLKit.ScanImage(filepath);
                    console.log("result from the promise:",result);
                    setScanned(true);
                    navigation.navigate('result',result);
                }
                catch(error){
                    console.log(error);
                }
            }
            onScan();
        }
    },[sharedFiles]);

    

    useEffect(()=>{
        if(Scanned){
            console.log("scanned");
        }
        else{
            console.log("waiting to be scanned");
        }
    },[Scanned]);


    return ( 
        
        <View 
            style={ImageScanStyles.container}
            >
            {!Scanned && <Text>
                    Scanning
                </Text>}
            {!Loaded && <Text>
                Loading
                </Text>}
            <Image
            style={ImageScanStyles.imgscan}
            source={{
                uri:'https://reactnative.dev/img/tiny_logo.png',
            }}
            onLoad={e =>{setLoaded(true);}}
            >

            </Image>
        </View>
     );
}

AppRegistry.registerComponent('imagescan',()=>ImageScan);

export default ImageScan;