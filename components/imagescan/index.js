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

    
    const [QrCodeFound, setQrCodeFound] = useState(false);
    const [Loaded, setLoaded] = useState(false);
    //if image has been scanned
    const [Scanned,setScanned] = useState(false);
    //if image had been processed
    const [Processed, setProcessed] = useState(false);

    const [imguri, setImguri] = useState(null);

    const [ImageLoaded, setImageLoaded] = useState(false);

    const [ImageLoadingError, setImageLoadingError] = useState(null);

    const sharedFiles = useContext(sharedFilesContext);

    

    useEffect(()=>{
        setTimeout(()=>{setScanned(true);},2000);

        if(sharedFiles){
            setImguri(sharedFiles[0].contentUri);

            let filepath = sharedFiles[0].filePath;
            console.log(filepath);
 
            MLKit.MyFunction(
                filepath,
                (error)=>{
                    console.log("This is an error",error);
                },
                (response)=>{
      
                  console.log("This is a success response",response);
      
                }
              ); 
        }

        console.log("shared files:",sharedFiles);
        

    },[sharedFiles]);

    

    useEffect(()=>{

        if(Scanned){
            console.log("scanned");
            navigation.push('result');
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