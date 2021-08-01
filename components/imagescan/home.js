import React from "react";

import { useState,useEffect,useContext } from "react";

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
        flex:1,
        backgroundColor:"teal",
    },
    scan_text:{
        color:"white",
        fontSize:25,
    }
});

const ImageScan = ({navigation}) => {

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
            <Text style={ImageScanStyles.scan_text}>
                    Open Image with this app
            </Text>
        </View>
     );
}

AppRegistry.registerComponent('imagescan',()=>ImageScan);

export default ImageScan;