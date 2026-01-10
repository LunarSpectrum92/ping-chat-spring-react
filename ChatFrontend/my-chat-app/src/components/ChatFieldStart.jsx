import React from "react";


const ChatFieldStart = ({ message, friend }) => {

    const initials = `${friend.firstName[0]}${friend.lastName[0]}`;

    const bgVariants = ['bg-error', 'bg-error-content', 'bg-base-200', 'bg-success-content', 'bg-info-content'];
    const randomBg = bgVariants[friend.firstName.length % bgVariants.length];

    return (

        <div className="">

        <div className="chat chat-start">
            <div className="chat-image avatar">
                <div className={` flex justify-center w-10 rounded-full text-neutral-content  ring ring-base-200 ring-offset-base-100 ring-offset-2 ${randomBg}`}>
                    <span className=" self-center text-l font-bold uppercase ">{initials}</span>
                </div>
            </div>




            <div className="chat-header mb-1">

                </div>
            <div className="chat-bubble chat-bubble-primary text-white">
                {message.content}
            </div>
            <div className="chat-footer opacity-50 mt-1">
                Dostarczono
            </div>
        </div>
        </div>
    );
};

export default ChatFieldStart;