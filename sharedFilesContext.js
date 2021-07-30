import ReceiveSharingIntent from "react-native-receive-sharing-intent";

import { useContext,useState,useEffect,useRef } from "react";


import { NativeModules, AppState } from 'react-native';

import React from "react";

export const sharedFilesContext = React.createContext();




const SharedFilesProvider = ({children}) => {

  const [sharedFiles, setSharedFiles] = useState();
  const appState = useRef(AppState.currentState);
  const [appStateVisible, setAppStateVisible] = useState(appState.current);
  

  const handleAppStateChange = (nextAppState) => {

    
    


    if (appState.current.match(/inactive|background/) && nextAppState === "active") {
      ReceiveSharingIntent.getReceivedFiles(files => {
        // files returns as JSON Array example
        //[{ filePath: null, text: null, weblink: null, mimeType: null, contentUri: null, fileName: null, extension: null }]
        setSharedFiles(files);
        console.log("set from inside handleasppstatechange",files.length);
        
      }, 
      (error) =>{
        console.log(error);
      }, 
      'imagescan' // share url protocol (must be unique to your app, suggest using your apple bundle id)
      )


      console.log("App has come to the foreground!");
      
    }

    appState.current = nextAppState;
    setAppStateVisible(appState.current);
    console.log("AppState", appState.current);

    /*if(appState.current.match(/inactive|background/)){
      console.log("clearing files");
      ReceiveSharingIntent.clearReceivedFiles();
    }*/
  }

  useEffect(() => {


    console.log("inside useeffect");
    AppState.addEventListener("change", handleAppStateChange);
    //console.log("inside useeffect");
    
    /* return ( () => {
      AppState.removeEventListener("change", handleAppStateChange);
    }) */ 
  },[]);


    return ( 
        <sharedFilesContext.Provider value={sharedFiles}>
            {children}
        </sharedFilesContext.Provider>
     );
}
 
export default SharedFilesProvider;