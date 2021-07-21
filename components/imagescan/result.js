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

const Result = ({navigation}) => {

    const [ResultData,setResultData] = useState(null);
    const [ResultText, setResultText] = useState("Result Not Found");

    useEffect(() => {
        if(ResultData){
            setResultText("Success");
        }
    },[ResultData,ResultText])


    
    return ( <>
        <View style={styles.container}>
            <Text>
                {ResultText}
            </Text>
        </View>
    </> );
}
 
export default Result;