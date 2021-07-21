import { StatusBar } from 'expo-status-bar';
import React from 'react';

import { useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';

import ImageScan from './components/imagescan'; 
import Result from './components/imagescan/result';

import { createStackNavigator } from '@react-navigation/stack';

import { NavigationContainer } from '@react-navigation/native';

import ReceiveSharingIntent from "react-native-receive-sharing-intent";
import { useEffect } from 'react/cjs/react.development';



import SharedFilesProvider from './sharedFilesContext';





const appStack = createStackNavigator();





export default function App() {

  



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
