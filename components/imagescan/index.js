import React from "react";

import { useState,useEffect } from "react";


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

    const [ImageLoaded, setImageLoaded] = useState(false);

    

    useEffect(()=>{
        setTimeout(()=>{setScanned(true);},2000);
        

        

        
        
        //setProcessed(true);
    });

    

    useEffect(()=>{

        if(Scanned){
            console.log("scanned");
            navigation.push('result');
        }
        else{
            console.log("waiting to be scanned");
        }


    },[Scanned]);

    useEffect(()=>{

        if(Processed){
            console.log("processed");
        }
        else{
            console.log("waiting to be processed");
        }

    },[Processed])




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