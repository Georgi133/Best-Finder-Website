import { useEffect, useState } from "react";
import { over } from 'stompjs';
import SockJS from "sockjs-client";
import { useAuthContext } from "../AuthContext/AuthContext";
import { MyNavBar } from "../Header/MyNavBar";
import style from './Chat.module.css';
import { Footer } from "../Footer/Footer";
import { useTranslation } from "react-i18next";

var stompClient = null

export const ChatRoom = () => {

    const { t, i18n } = useTranslation();
    const [privateChats, setPrivateChats] = useState(new Map());  
    const { fullName, getUserFullName} = useAuthContext();
    const [publicChats, setPublicChats] = useState([]); 
    const [tab,setTab] =useState("CHATROOM");
    const [userData, setUserData] = useState({
        username: '',
        receivername: '',
        connected: false,
        message: ''
      });
    useEffect(() => {
        if(!fullName) {
        getUserFullName();
        }
    }, [fullName]);

    const connect =()=>{
        let Sock = new SockJS('http://localhost:8080/ws');
        stompClient = over(Sock);
        stompClient.connect({},onConnected, onError);
    }

    const onConnected = () => {
        setUserData({...userData,"connected": true});
        stompClient.subscribe('/chatroom/public', onMessageReceived);
        stompClient.subscribe('/user/'+fullName+'/private', onPrivateMessage);
        userJoin();
    }

    const userJoin=()=>{
          var chatMessage = {
            senderName: fullName,
            status:"JOIN"
          };
          stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
    }

    const onMessageReceived = (payload)=>{
        var payloadData = JSON.parse(payload.body);
        switch(payloadData.status){
            case "JOIN":
                if(!privateChats.get(payloadData.senderName)){
                    privateChats.set(payloadData.senderName,[]);
                    setPrivateChats(new Map(privateChats));
                }
                break;
            case "MESSAGE":
                if(!privateChats.get(payloadData.senderName)){
                    privateChats.set(payloadData.senderName,[]);
                    setPrivateChats(new Map(privateChats));
                }
                publicChats.push(payloadData);
                setPublicChats([...publicChats]);
                break;
        }
    }
    
    const onPrivateMessage = (payload)=>{
        console.log(payload);
        var payloadData = JSON.parse(payload.body);
        if(privateChats.get(payloadData.senderName)){
            privateChats.get(payloadData.senderName).push(payloadData);
            setPrivateChats(new Map(privateChats));
        }else{
            let list =[];
            list.push(payloadData);
            privateChats.set(payloadData.senderName,list);
            setPrivateChats(new Map(privateChats));
        }
    }

    const onError = (err) => {
        console.log(err);
        
    }

    const handleMessage =(event)=>{
        const {value}=event.target;
        setUserData({...userData,"message": value});
    }
    const sendValue=()=>{
            if (stompClient) {
              var chatMessage = {
                senderName: fullName,
                message: userData.message,
                status:"MESSAGE"
              };
              console.log(chatMessage);
              stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
              setUserData({...userData,"message": ""});
            }
    }

    const sendPrivateValue=()=>{
        if (stompClient) {
          var chatMessage = {
            senderName: fullName,
            receiverName:tab,
            message: userData.message,
            status:"MESSAGE"
          };
          
          if(fullName !== tab){
            privateChats.get(tab).push(chatMessage);
            setPrivateChats(new Map(privateChats));
          }
          stompClient.send("/app/private-message", {}, JSON.stringify(chatMessage));
          setUserData({...userData,"message": ""});
        }
    }

    const handleUsername=(event)=>{
        const {value}=event.target;
        setUserData({...userData,"username": value});
    }

    const registerUser=()=>{
        connect();
    }
    return (
        <>
        <MyNavBar />
    <div className={style.container}>
        {userData.connected ?
        
        <div className="chat-box">
            <div className="member-list">
                <ul>
                    <li onClick={()=>{setTab("CHATROOM")}} className={`member ${tab==="CHATROOM" && "active"}`}>Chatroom - Everyone who is inside the chat</li>
                    {[...privateChats.keys()].map((name,index)=>(
                        <li onClick={()=>{setTab(name)}} className={`member ${tab===name && "active"}`} key={index}>{name === fullName ? `Your chat - ${name}` : 'Chat to - ' + name}</li>
                    ))}
                </ul>
            </div>
            <h2 className="chatHeader">Chat: </h2>
            {tab==="CHATROOM" && <div className="chat-content">
                <ul className="chat-messages">
                    {publicChats.map((chat,index)=>(
                        <li className={`message ${chat.senderName === fullName && "self"}`} key={index}>
                            {chat.senderName !== fullName && <div onClick={() => {setTab(chat.senderName)}} className="avatar">{chat.senderName}</div>}
                            {chat.senderName === fullName && <div className="avatar self">You</div>}
                            {/* {chat.senderName} */}
                            <div className="message-data">{chat.message}</div>
                            
                        </li>
                    ))}
                </ul>

                <div className="send-message">
                    <input type="text" className="input-message" placeholder="enter the message" value={userData.message} onChange={handleMessage} /> 
                    <button type="button" className="send-button" onClick={sendValue}>{t("userForm.submit")}</button>
                </div>
            </div>}
            {tab!=="CHATROOM" && <div className="chat-content">
                <ul className="chat-messages">
                    {[...privateChats.get(tab)].map((chat,index)=>(
                        <li className={`message ${chat.senderName === fullName && "self"}`} key={index}>
                            {chat.senderName !== fullName && <div className="avatar">{chat.senderName}</div>}
                            {chat.senderName === fullName && <div className="avatar self">You</div>}
                            <div className="message-data">{chat.message}</div>
                            
                        </li>
                    ))}
                </ul>

                <div className="send-message">
                    <input type="text" className="input-message" placeholder="enter the message" value={userData.message} onChange={handleMessage} /> 
                    <button type="button" className="send-button" onClick={sendPrivateValue}>{t("userForm.submit")}</button>
                </div>
            </div>}
        </div>
        :
        <div className="register">
            <h2>Chat Entrance !</h2>
            <input
                id="user-name"
                placeholder="Enter your name"
                name="userName"
                value={fullName}
                onChange={handleUsername}
                margin="normal"
              />
              <button className="connect" type="button" onClick={registerUser}>
                    {t("userForm.submit")}
              </button> 
        </div>}
    </div>
    <Footer />
    </>
    )
}