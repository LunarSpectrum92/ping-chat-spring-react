import ChatFieldStart from "./ChatFieldStart.jsx";
import ChatFieldEnd from "./ChatFieldEnd.jsx";
import InputField from "./InputField.jsx";
import SockJS from "sockjs-client";
import { Client } from '@stomp/stompjs';
import useAuth from "../hooks/useAuth.jsx";
import {useEffect, useState, useRef, useCallback} from "react";



const Chat = ({ messages, currentFriendId, friend }) => {

    const { auth } = useAuth();
    const [inputValue, setInputValue] = useState("");
    const [finalMessages, setFinalMessages] = useState([]);
    const stompClientRef = useRef(null);
    const messagesEndRef = useRef(null);


    useEffect(() => {
        if (messagesEndRef.current) {
            messagesEndRef.current.scrollTop = messagesEndRef.current.scrollHeight;
        }
    }, [finalMessages]);

    useEffect(() => {
        setFinalMessages(messages || []);
    }, [messages, currentFriendId]);

    useEffect(() => {
        if (!currentFriendId || !friend?.AuthId || !auth?.accessToken) return;
        // let userId;
        // for(let mess of messages) {
        //     if(mess.sender != currentFriendId){
        //         userId = mess.sender;
        //         break;
        //     }
        // }

        const client = new Client({
            webSocketFactory: () => new SockJS(`http://localhost:8080/ws?authId=${auth.AuthId}&token=${auth.accessToken}`),
            connectHeaders: {
                Authorization: `Bearer ${auth.accessToken}`,
            },
            debug: (str) => console.log(str),
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
        });
//${auth.AuthId}
        client.onConnect = (frame) => {
            console.log("Połączono z WebSocket");
//userId
            const conversationId = createConversationId();
            client.subscribe(`/user/${auth.AuthId}/queue/chat/${conversationId}`, (message) => {
                const receivedMsg = JSON.parse(message.body);
//userId
                const newMsgObject = {
                    // id: Date.now(),
                    content: receivedMsg.content,
                    sender: receivedMsg.sender,
                    recipient: auth.AuthId,
                    conversationId: receivedMsg.conversationId || ""
                };

                setFinalMessages((prev) => [...prev, newMsgObject]);
            });
        };

        client.onStompError = (frame) => {
            console.error("Błąd STOMP:", frame.headers['message']);
        };


        client.activate();
        stompClientRef.current = client;

        return () => {
            if (stompClientRef.current) {
                stompClientRef.current.deactivate();
                console.log("Rozłączono");
            }
        };
    }, [friend?.AuthId, auth?.accessToken]);

    // 3. Wysyłanie wiadomości
    const sendPrivateMessage = (e) => {
        e.preventDefault();

        if (!inputValue.trim() || !stompClientRef.current?.connected) return;
        const conversationId = createConversationId();
        const msgPayload = {
            sender: auth.AuthId,
            recipient: friend.AuthId,
            content: inputValue,
            conversationId: conversationId,
        };

        stompClientRef.current.publish({
            destination: '/app/private-message',
            body: JSON.stringify(msgPayload),
        });

        const myMsg = {
            id: Date.now(),
            ...msgPayload
        };
        // setFinalMessages((prev) => [...prev, myMsg]);
        setInputValue("");
    };


    const createConversationId = useCallback(() => {
        if (messages?.length > 0 && messages[0].conversationId) {
            return messages[0].conversationId;
        }

        if (!currentFriendId || !auth?.AuthId) {
            return null;
        }

        const list = [currentFriendId, auth.AuthId].sort();
        return `${list[0]}_${list[1]}`;
    }, [messages, currentFriendId, auth?.AuthId]);


        if (!currentFriendId) {
            return <div className="p-10 text-center">choose friend, to start conversation</div>;
        }

    // overflow-hidden
    return (
        <div className="flex flex-col h-[calc(100vh-115px)] md:h-[calc(100vh-80px)] lg:h-[calc(100vh-64px)] bg-base-100">
            <div className="flex-1 min-h-0 overflow-y-auto p-4" ref={messagesEndRef}>
            {finalMessages.length > 0 ? (
                finalMessages.map((msg) => (
                    msg.sender == currentFriendId ?
                        <ChatFieldStart key={msg.id} message={msg} friend={friend} /> :
                        <ChatFieldEnd key={msg.id} message={msg} friend={friend} />
                )
                )
            ) : (
                <div className="text-center opacity-50">Brak wiadomości. Przywitaj się!</div>
            )}
                <div ref={messagesEndRef}></div>

        </div>
            <div className="p-4 border-t border-base-300 bg-base-100 flex-none sticky bottom-0">
                <form className="max-w-[800px] mx-auto flex gap-2" onSubmit={sendPrivateMessage}>
                    <input
                        type="text"
                        placeholder="Napisz wiadomość..."
                        className="input input-bordered flex-grow"
                        value={inputValue}
                        onChange={(e) => setInputValue(e.target.value)}
                        required
                    />
                    <button className="btn btn-primary btn-square" type="submit" onClick = {() => {scroll}}>
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" />
                        </svg>
                    </button>
                </form>
            </div>
        </div>
    );
};


export default Chat;