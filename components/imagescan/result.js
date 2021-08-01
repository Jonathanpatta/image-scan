import React from "react";

import { useState,useEffect } from "react";

import{
    View,
    Image,
    Text,
    StyleSheet,
    AppRegistry,
} from "react-native"

const styles = StyleSheet.create({
    container:{
        alignItems:"center",
        justifyContent:"center",
        backgroundColor:"teal",
        flex:1,
    },
    result_text:{
        color:"white",
        fontSize:25,
    }
})

const Result = ({route,navigation}) => {

    const [ResultText, setResultText] = useState("Result Not Found");

    const resultData = route.params

    useEffect(() => {
        if(resultData){
            setResultText(resultData.data[0].rawValue);
        }
    })


    
    return ( <>
        <View style={styles.container}>
            <Text style={styles.result_text}>
                {ResultText}
            </Text>
        </View>
    </> );
}
 
export default Result;