import React from "react";


const ChatFieldEnd = ({message , friend }) =>{

    const initials = `${friend.firstName[0]}${friend.lastName[0]}`;

    const bgVariants = ['bg-error', 'bg-error-content', 'bg-base-200', 'bg-success-content', 'bg-info-content'];
    const randomBg = bgVariants[friend.firstName.length % bgVariants.length];



    return(
        <div className="">
            <div className="chat chat-end">
                <div className="chat-image avatar">
                    <div className={` flex justify-center w-10 rounded-full text-neutral-content  ring ring-base-200 ring-offset-base-300 ring-offset-2 bg-base-100`}>
                        <span className=" self-center text-l font-bold uppercase "><svg
                            xmlns="http://www.w3.org/2000/svg"
                            viewBox="0 0 24 24"
                            fill="none"
                            stroke="black"
                            strokeWidth="2"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            className="size-5"
                        >
            <path d="M12 7m-4 0a4 4 0 1 0 8 0a4 4 0 1 0 -8 0" />
            <path d="M6 21v-2a4 4 0 0 1 4 -4h4a4 4 0 0 1 4 4v2" />
        </svg></span>
                    </div>
                </div>
                <div className="chat-header">

                </div>
                <div className="chat-bubble">
                        {message.content}
                </div>
                <div className="chat-footer opacity-50"></div>
            </div>
        </div>
    );

}



export default ChatFieldEnd;