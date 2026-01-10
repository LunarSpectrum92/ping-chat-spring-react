import Navbar from "../components/NavBar.jsx";
import usePrivateAxios from "../hooks/usePrivateAxios";
import InvitationField from "../components/InvitationField.jsx";
import { useEffect, useState } from "react";

const Invitations = () => {
    const [invitations, setInvitations] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const axiosPrivate = usePrivateAxios();

    useEffect(() => {
        const fetchInvitations = async () => {
            try {
                const result = await axiosPrivate.get('/user/invitation/invitations');
                setInvitations(result.data);
            } catch (e) {
                console.error("Error fetching invitations:", e);
            } finally {
                setIsLoading(false);
            }
        };
        fetchInvitations();
    }, [axiosPrivate]);

    const handleActionComplete = (authId) => {
        setInvitations(prev => prev.filter(user => user.AuthId !== authId));
    };

    return (
        <div>
            <Navbar>
        <div className="min-h-screen">


            <main className="container mx-auto px-4 py-8">
                {/* Header */}
                <div className="text-center mb-10">
                    <h2 className="text-3xl font-bold">Your Friend Invitations</h2>
                    <div className="divider w-24 mx-auto mt-2 h-1 bg-primary rounded-full"></div>
                </div>

                {/* Content Area */}
                <div className="flex flex-col items-center gap-4">
                    {isLoading ? (
                        <div className="flex flex-col items-center mt-20 gap-4">
                            <span className="loading loading-spinner loading-lg text-primary"></span>
                            <p className="text-lg font-medium animate-pulse">Loading invitations...</p>
                        </div>
                    ) : invitations.length > 0 ? (
                        <div className="w-full flex flex-col items-center gap-2">
                            {invitations.map(user => (
                                <InvitationField
                                    user={user}
                                    key={user.AuthId}
                                    onActionComplete={handleActionComplete}
                                />
                            ))}
                        </div>
                    ) : (
                        <div className="card w-full max-w-md bg-base-100 shadow-sm border border-base-300 mt-10">
                            <div className="card-body items-center text-center py-12">
                                <div className="text-6xl mb-4 text-base-content/20">📭</div>
                                <h3 className="text-xl font-semibold">All caught up!</h3>
                                <p className="text-base-content/60">
                                    You have no pending invitations at the moment.
                                </p>
                            </div>
                        </div>
                    )}
                </div>
            </main>
        </div>
            </Navbar>
        </div>
    );
}

export default Invitations;