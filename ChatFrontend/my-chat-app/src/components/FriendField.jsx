import React, { useState } from 'react';
import usePrivateAxios from "../hooks/usePrivateAxios";
import useAuth from "../hooks/useAuth";

const UserField = ({ firstName, lastName, AuthId, onClick }) => {
    const initials = `${firstName[0]}${lastName[0]}`;

    const bgVariants = ['bg-error', 'bg-error-content', 'bg-base-200', 'bg-success-content', 'bg-info-content'];
    const randomBg = bgVariants[firstName.length % bgVariants.length];



    return (
        <div
            onClick={onClick}
            className="flex justify-center w-full px-4 my-2 cursor-pointer transition-all active:scale-[0.98]"
        >
            <div className="card card-side bg-base-100 shadow-xl border border-base-200 w-full max-w-lg overflow-hidden flex-col sm:flex-row hover:bg-base-200 transition-colors">

                <div className="card-body flex-row items-center gap-4 p-4 sm:p-6">

                    <div className="avatar placeholder flex-shrink-0">
                        <div className={`flex justify-center ${randomBg} text-neutral-content rounded-full w-16 h-16 ring ring-base-200 ring-offset-base-100 ring-offset-2`}>
                            <span className=" self-center text-xl font-bold uppercase ">{initials}</span>
                        </div>
                    </div>

                    <div className="flex-1 min-w-0 text-center sm:text-left">
                        <h2 className="card-title text-lg font-bold block truncate">
                            {firstName} {lastName}
                        </h2>
                        <p className="text-sm text-base-content/60">System User</p>
                    </div>

                    <div className="card-actions justify-center sm:justify-end w-full sm:w-auto mt-4 sm:mt-0">
                    </div>

                </div>
            </div>
        </div>
    );
};

export default UserField;