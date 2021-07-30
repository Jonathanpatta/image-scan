import { StatusBar } from 'expo-status-bar';
import React from 'react';

import { useState ,useEffect,useRef} from 'react';
import { StyleSheet, Text, View } from 'react-native';

import ImageScan from './components/imagescan'; 
import Result from './components/imagescan/result';

import { createStackNavigator } from '@react-navigation/stack';

import { NavigationContainer } from '@react-navigation/native';

import ReceiveSharingIntent from "react-native-receive-sharing-intent";


import SharedFilesProvider from './sharedFilesContext';

import { AppState } from "react-native";







const appStack = createStackNavigator();





export default function App() {

  
  const onGetIntentFiles = (files) => {
    console.log(files);
    if(files){
      let filePath = files[0].filePath;
      console.log(filePath);

      const onScan = async () => {
        try{
          const result = await MLKit.ScanImage(filepath);
          console.log("result from the promise:",result);
        }
        catch(error){
            console.log(error);
        }
      }
    }
    
  }

  const onIntentFilesError = (error) => {
    console.log(error);
  }

  const handleAppStateChange = (nextAppState) => {
    
  }

  useEffect(() => {
    
  },[])

  



  return (

    <SharedFilesProvider>
      <NavigationContainer>
        <appStack.Navigator>
          <appStack.Screen
            name="image scan"
            component={ImageScan}
          />
          <appStack.Screen
            name="result"
            component={Result}
          />
        </appStack.Navigator>
      </NavigationContainer>
    </SharedFilesProvider>
    
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
