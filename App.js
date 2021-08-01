import React from 'react';

import ImageScan from './components/imagescan/home'; 
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
              options={({ route }) => ({ title: route.params.data==null?"error":(route.params.data.length == 0?"unknown":route.params.data[0].valueType) })}
            />
          </appStack.Navigator>
        </NavigationContainer>
      
    </SharedFilesProvider>
    
  );
}
