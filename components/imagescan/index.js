import React from "react";

import { useState,useEffect,useContext } from "react";

import RecieveSharingIntent from "react-native-receive-sharing-intent";

import { sharedFilesContext } from "../../sharedFilesContext";



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
        }

        console.log("shared files:",sharedFiles);
        

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

        alert("error with loading the image");

        

    },[ImageLoadingError]);




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
                uri:imguri,
            }}
            onLoad={e =>{setLoaded(true);}}
            >

            </Image>
        </View>
     );
}

AppRegistry.registerComponent('imagescan',()=>ImageScan);

export default ImageScan;