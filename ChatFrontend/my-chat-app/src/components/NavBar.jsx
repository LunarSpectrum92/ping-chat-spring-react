import React from 'react';
import { Link } from 'react-router-dom';
import useAuth from "../hooks/useAuth.jsx";
import { authPublic } from "../api/auth.js";

const NavBar = ({ children }) => {
    const { setAuth } = useAuth();

    const HandleLogout = async () => {
        try {
            await authPublic.post('/logout');
            setAuth({});
        } catch (err) {
            console.error("Error in logout", err);
        }
    }

    return (
        <div className="drawer lg:drawer-open min-h-screen">
            <input id="my-drawer-4" type="checkbox" className="drawer-toggle" />
            <div className="drawer-content flex flex-col min-h-0">
                <nav className="navbar h-16 flex-none bg-base-300">
                    <label htmlFor="my-drawer-4" aria-label="open sidebar" className="btn btn-square btn-ghost">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="size-5"><path d="M4 6l16 0" /><path d="M4 12l16 0" /><path d="M4 18l12 0" /></svg>
                    </label>
                    <div className="px-4 font-bold">Ping</div>
                </nav>
                <div className="flex-1 min-h-0 overflow-hidden">{children}</div>
            </div>

            <div className="drawer-side is-drawer-close:overflow-visible">
                <label htmlFor="my-drawer-4" aria-label="close sidebar" className="drawer-overlay"></label>
                <div className="flex min-h-full flex-col items-start bg-base-200 is-drawer-close:w-14 is-drawer-open:w-64">
                    <ul className="menu w-full grow gap-2">
                        <li>
                            <Link to={'/users'} className="is-drawer-close:tooltip is-drawer-close:tooltip-right" data-tip="Users">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="size-5"><path d="M9 7m-4 0a4 4 0 1 0 8 0a4 4 0 1 0 -8 0" /><path d="M3 21v-2a4 4 0 0 1 4 -4h4a4 4 0 0 1 4 4v2" /><path d="M16 3.13a4 4 0 0 1 0 7.75" /><path d="M21 21v-2a4 4 0 0 0 -3 -3.85" /></svg>
                                <span className="is-drawer-close:hidden">Users</span>
                            </Link>
                        </li>

                        <li>
                            <Link to={'/invitations'} className="is-drawer-close:tooltip is-drawer-close:tooltip-right" data-tip="Invitations">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="size-5"><path d="M10 21h-6a2 2 0 0 1 -2 -2v-12a2 2 0 0 1 2 -2h16a2 2 0 0 1 2 2v3" /><path d="M3 7l9 6l9 -6" /><path d="M15 19l2 2l4 -4" /></svg>
                                <span className="is-drawer-close:hidden">Invitations</span>
                            </Link>
                        </li>

                        <li>
                            <Link to={'/messages'} className="is-drawer-close:tooltip is-drawer-close:tooltip-right" data-tip="Messages">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="size-5"><path d="M3 20l1.3 -3.9a9 8 0 1 1 3.4 2.9l-4.7 1" /></svg>
                                <span className="is-drawer-close:hidden">Messages</span>
                            </Link>
                        </li>

                        <div className="grow"></div> {/* Spacer pushing logout to bottom */}

                        <li>
                            <button className="text-error hover:bg-error hover:text-white is-drawer-close:tooltip is-drawer-close:tooltip-right" onClick={HandleLogout} data-tip="Logout">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="size-5"><path d="M14 8v-2a2 2 0 0 0 -2 -2h-7a2 2 0 0 0 -2 2v12a2 2 0 0 0 2 2h7a2 2 0 0 0 2 -2v-2" /><path d="M9 12h12l-3 -3" /><path d="M18 15l3 -3" /></svg>
                                <span className="is-drawer-close:hidden">Logout</span>
                            </button>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default NavBar;