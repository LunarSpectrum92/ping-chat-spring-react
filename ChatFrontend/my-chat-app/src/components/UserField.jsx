import React, { useState } from 'react';
import usePrivateAxios from "../hooks/usePrivateAxios";
import useAuth from "../hooks/useAuth";

const UserField = (user) => {
    const initials = `${user.firstName[0]}${user.lastName[0]}`;

    const bgVariants = ['bg-error', 'bg-error-content', 'bg-base-200', 'bg-success-content', 'bg-info-content'];
    const randomBg = bgVariants[user.firstName.length % bgVariants.length];

    const { auth } = useAuth();
    const [isClicked, setIsClicked] = useState(false);
    const [loading, setLoading] = useState(false);
    const axiosPrivate = usePrivateAxios();

    const handleInvite = async () => {
        if (isClicked || loading) return;
        try {
            setLoading(true);
            await axiosPrivate.post('/user/invitation/send', { reciverId: user.AuthId });
            setIsClicked(true);
        } catch (err) {
            console.error("Error sending invite", err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex justify-center w-full px-4 my-2">
            <div className="card card-side bg-base-100 shadow-xl border border-base-200 w-full max-w-lg overflow-hidden flex-col sm:flex-row">

                <div className="card-body flex-row items-center gap-4 p-4 sm:p-6">

                    <div className="avatar placeholder flex-shrink-0">
                        <div className={`flex justify-center ${randomBg} text-neutral-content rounded-full w-16 h-16 ring ring-base-200 ring-offset-base-100 ring-offset-2`}>
                            <span className=" self-center text-xl font-bold uppercase ">{initials}</span>
                        </div>
                    </div>

                    <div className="flex-1 min-w-0 text-center sm:text-left">
                        <h2 className="card-title text-lg font-bold block truncate">
                            {user.firstName} {user.lastName}
                        </h2>
                        <p className="text-sm text-base-content/60">System User</p>
                    </div>

                    <div className="card-actions justify-center sm:justify-end w-full sm:w-auto mt-4 sm:mt-0">
                        <button
                            onClick={handleInvite}
                            disabled={isClicked || loading}
                            className={`btn btn-sm sm:btn-md min-w-[100px] transition-all duration-300 ${
                                isClicked
                                    ? "btn-disabled bg-base-300 text-base-content/50"
                                    : "btn-primary"
                            } ${loading ? "loading" : ""}`}
                        >
                            {isClicked ? "Invited" : loading ? "" : "Invite"}
                        </button>
                    </div>

                </div>
            </div>
        </div>
    );
};

export default UserField;