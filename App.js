import { StatusBar } from 'expo-status-bar';
import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

import ImageScan from './components/imagescan'; 
import Result from './components/imagescan/result';

import { createStackNavigator } from '@react-navigation/stack';

import { NavigationContainer } from '@react-navigation/native';

import ReceiveSharingIntent from "react-native-receive-sharing-intent"

const appStack = createStackNavigator();

ReceiveSharingIntent.getReceivedFiles(files => {
  // files returns as JSON Array example
  //[{ filePath: null, text: null, weblink: null, mimeType: null, contentUri: null, fileName: null, extension: null }]

  console.log(files);
}, 
(error) =>{
  console.log(error);
}, 
'ShareMedia' // share url protocol (must be unique to your app, suggest using your apple bundle id)
);


// To clear Intents
ReceiveSharingIntent.clearReceivedFiles();

export default function App() {
  return (
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
