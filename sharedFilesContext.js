import ReceiveSharingIntent from "react-native-receive-sharing-intent";

import { useContext,useState,useEffect } from "react";


import { NativeEventEmitter,NativeModules } from 'react-native';

import React from "react";

export const sharedFilesContext = React.createContext();




const SharedFilesProvider = ({children}) => {

    const [sharedFiles, setSharedFiles] = useState();

  useEffect(() => {
    ReceiveSharingIntent.getReceivedFiles(files => {
      // files returns as JSON Array example
      //[{ filePath: null, text: null, weblink: null, mimeType: null, contentUri: null, fileName: null, extension: null }]
    
      //console.log(files);

      setSharedFiles(files);
    
    
    }, 
    (error) =>{
      console.log(error);
    }, 
    'ShareMedia' // share url protocol (must be unique to your app, suggest using your apple bundle id)
    );

    const eventEmitter = new NativeEventEmitter(NativeModules.MLKit);
    const eventListener = eventEmitter.addListener('ScanResponse', (event) => {
            console.log(event); 
        });

    
    return ( () => {
      ReceiveSharingIntent.clearReceivedFiles();
      
      console.log("cleared files");
    })
  });


    return ( 
        <sharedFilesContext.Provider value={sharedFiles}>
            {children}
        </sharedFilesContext.Provider>
     );
}
 
export default SharedFilesProvider;