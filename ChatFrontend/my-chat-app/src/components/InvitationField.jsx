import React, { useState } from 'react';
import usePrivateAxios from "../hooks/usePrivateAxios";

const InvitationField = ({ user, onActionComplete }) => {
    const initials = `${user.firstName[0]}${user.lastName[0]}`;

    const bgVariants = ['bg-error', 'bg-error-content', 'bg-base-200', 'bg-success-content', 'bg-info-content'];
    const randomBg = bgVariants[user.firstName.length % bgVariants.length];

    const [loading, setLoading] = useState(false);
    const [actionType, setActionType] = useState(null);
    const [status, setStatus] = useState(null);
    const axiosPrivate = usePrivateAxios();

    const handleAccept = async () => {
        if (loading) return;
        try {
            setLoading(true);
            setActionType('accepting');
            await axiosPrivate.post('/user/invitation/accept', { reciverId: user.AuthId });
            setStatus('accepted');
            if (onActionComplete) onActionComplete(user.AuthId);
        } catch (err) {
            console.error("Error accepting invitation", err);
            setLoading(false);
            setActionType(null);
        }
    };

    const handleDecline = async () => {
        if (loading) return;
        try {
            setLoading(true);
            setActionType('declining');
            await axiosPrivate.post('/user/invitation/decline', { reciverId: user.AuthId });
            setStatus('declined');
            if (onActionComplete) onActionComplete(user.AuthId);
        } catch (err) {
            console.error("Error declining invitation", err);
            setLoading(false);
            setActionType(null);
        }
    };

    if (status === 'accepted') return null;

    return (
        <div className="flex justify-center w-full px-4 my-4">
            <div className="card card-side bg-base-100 shadow-xl border border-base-200 w-full max-w-lg overflow-hidden sm:flex-row flex-col">

                <div className="card-body flex-row items-center gap-4 p-4 sm:p-6">
                    <div className="avatar placeholder">
                        <div className={`flex justify-center ${randomBg} text-neutral-content rounded-full w-16 h-16 ring ring-base-200 ring-offset-base-100 ring-offset-2`}>
                            <span className="self-center text-xl font-bold uppercase">{initials}</span>
                        </div>
                    </div>

                    <div className="flex-1 min-w-0">
                        <h2 className="card-title text-base sm:text-lg truncate">
                            {user.firstName} {user.lastName}
                        </h2>
                        <p className="text-sm text-base-content/60">Sent you an invitation</p>
                    </div>

                    <div className="card-actions justify-end flex-nowrap gap-2">
                        <button
                            onClick={handleAccept}
                            disabled={loading}
                            className={`btn btn-success btn-sm sm:btn-md normal-case ${loading && actionType === 'accepting' ? 'loading' : ''}`}
                        >
                            {!loading || actionType !== 'accepting' ? 'Accept' : ''}
                        </button>

                        <button
                            onClick={handleDecline}
                            disabled={loading}
                            className={`btn btn-outline btn-error btn-sm sm:btn-md normal-case ${loading && actionType === 'declining' ? 'loading' : ''}`}
                        >
                            {loading && actionType === 'declining' ? '' : 'Decline'}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default InvitationField;