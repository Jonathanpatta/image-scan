import { StatusBar } from 'expo-status-bar';
import React from 'react';

import { useState ,useEffect,useRef} from 'react';
import { StyleSheet, Text, View } from 'react-native';

import ImageScan from './components/imagescan'; 
import Result from './components/imagescan/result';

import { createStackNavigator } from '@react-navigation/stack';

import { NavigationContainer } from '@react-navigation/native';
import SharedFilesProvider from './sharedFilesContext';


const appStack = createStackNavigator();

export default function App() {


  return (

    <SharedFilesProvider>
      
        <NavigationContainer>
          <appStack.Navigator screenOptions={{
              headerTransparent:true,
              headerStyle:{
                
              },
              headerTitleAlign:"center",
              headerTintColor:"white",

            }}>
            <appStack.Screen
              name="image scan"
              component={ImageScan}
              
            />
            <appStack.Screen
              name="result"
              component={Result}
              options={({ route }) => ({ title: route.params.data[0].valueType })}
            />
          </appStack.Navigator>
        </NavigationContainer>
      
    </SharedFilesProvider>
    
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
