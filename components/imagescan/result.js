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
    },
    result_text:{
        backgroundColor:"red",
        fontSize:20
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
            <Text>
                {ResultText}
            </Text>
        </View>
    </> );
}
 
export default Result;