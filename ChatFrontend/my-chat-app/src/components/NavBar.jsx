import React from 'react';
import {Link} from 'react-router-dom';
import usePrivateAxios from "../hooks/usePrivateAxios.jsx";
import useAuth from "../hooks/useAuth.jsx";
import {authPublic} from "../api/auth.js";

const NavBar = ({children}) => {


  const {setAuth} = useAuth();




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
              {/* Navbar */}
              <nav className="navbar h-16 flex-none bg-base-300">
                  <label htmlFor="my-drawer-4" aria-label="open sidebar" className="btn btn-square btn-ghost">
                      {/* Sidebar toggle icon */}
                      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" strokeLinejoin="round" strokeLinecap="round" strokeWidth="2" fill="none" stroke="currentColor" className="my-1.5 inline-block size-4"><path d="M4 4m0 2a2 2 0 0 1 2 -2h12a2 2 0 0 1 2 2v12a2 2 0 0 1 -2 2h-12a2 2 0 0 1 -2 -2z"></path><path d="M9 4v16"></path><path d="M14 10l2 2l-2 2"></path></svg>
                  </label>
                  <div className="px-4">Navbar Title</div>
              </nav>
              <div className="flex-1 min-h-0 overflow-hidden">{children}</div>
          </div>

          <div className="drawer-side is-drawer-close:overflow-visible">
              <label htmlFor="my-drawer-4" aria-label="close sidebar" className="drawer-overlay"></label>
              <div className="flex min-h-full flex-col items-start bg-base-200 is-drawer-close:w-14 is-drawer-open:w-64">
                  <ul className="menu w-full grow">
                      <li>
                          <Link to={'/users'} className="is-drawer-close:tooltip is-drawer-close:tooltip-right" >
                              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" strokeLinejoin="round" strokeLinecap="round" strokeWidth="2" fill="none" stroke="currentColor" className="my-1.5 inline-block size-4"><path d="M15 21v-8a1 1 0 0 0-1-1h-4a1 1 0 0 0-1 1v8"></path><path d="M3 10a2 2 0 0 1 .709-1.528l7-5.999a2 2 0 0 1 2.582 0l7 5.999A2 2 0 0 1 21 10v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path></svg>
                              <span className="is-drawer-close:hidden">Users</span>
                          </Link>
                      </li>
                      <li>
                          <Link to={'/invitations'} className="is-drawer-close:tooltip is-drawer-close:tooltip-right">
                              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" strokeLinejoin="round" strokeLinecap="round" strokeWidth="2" fill="none" stroke="currentColor" className="my-1.5 inline-block size-4"><path d="M20 7h-9"></path><path d="M14 17H5"></path><circle cx="17" cy="17" r="3"></circle><circle cx="7" cy="7" r="3"></circle></svg>
                              <span className="is-drawer-close:hidden">Invitations</span>
                          </Link>
                      </li>
                      <li>
                          <Link to={'/messages'} className="is-drawer-close:tooltip is-drawer-close:tooltip-right" >
                              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" strokeLinejoin="round" strokeLinecap="round" strokeWidth="2" fill="none" stroke="currentColor" className="my-1.5 inline-block size-4"><path d="M15 21v-8a1 1 0 0 0-1-1h-4a1 1 0 0 0-1 1v8"></path><path d="M3 10a2 2 0 0 1 .709-1.528l7-5.999a2 2 0 0 1 2.582 0l7 5.999A2 2 0 0 1 21 10v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path></svg>
                              <span className="is-drawer-close:hidden">Messages</span>
                          </Link>
                      </li>
                      <li>
                          <button className="bg-error is-drawer-close:tooltip is-drawer-close:tooltip-right" onClick={HandleLogout} data-tip="Settings">
                              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" strokeLinejoin="round" strokeLinecap="round" strokeWidth="2" fill="none" stroke="currentColor" className="my-1.5 inline-block size-4"><path d="M20 7h-9"></path><path d="M14 17H5"></path><circle cx="17" cy="17" r="3"></circle><circle cx="7" cy="7" r="3"></circle></svg>
                              <span className="is-drawer-close:hidden">Logout</span>
                          </button>
                      </li>
                  </ul>
              </div>
          </div>
      </div>
    //
    //
    //
    //
    //
    // <Navbar expand="lg" bg="" className="bg-body" style={{height: '6vh'}}>
    //   <Container>
    //     <Navbar.Brand>Chat</Navbar.Brand>
    //     <Navbar.Toggle aria-controls="basic-navbar-nav" />
    //     <Navbar.Collapse id="basic-navbar-nav">
    //       <Nav
    //         className="me-auto my-2 my-lg-0"
    //         style={{ maxHeight: '100px' }}
    //       >
    //         <Nav.Link><Link to={'/'}>Users</Link></Nav.Link>
    //         <Nav.Link><Link to={'/invitations'}>Invitations</Link></Nav.Link>
    //       </Nav>
    //
    //         <Link to={`/Messages`}>
    //       <Button
    //         variant="outline-dark"
    //         style={{
    //           backgroundColor: 'orange',
    //           border: 'none',
    //           color: 'white',
    //           transition: 'all 0.3s ease',
    //         }}
    //         onMouseOver={(e) => {
    //           e.target.style.border = 'none';
    //           e.target.style.backgroundColor = 'orange';
    //           e.target.style.color = 'black';
    //         }}
    //         onMouseOut={(e) => {
    //           e.target.style.backgroundColor = 'orange';
    //           e.target.style.color = 'white';
    //         }}
    //       >
    //         Messages
    //       </Button>
    //           <Button
    //               variant="outline-dark"
    //               style={{
    //                 backgroundColor: 'red',
    //                 border: 'none',
    //                 color: 'white',
    //                 transition: 'all 0.3s ease',
    //                 margin: '10px',
    //               }}
    //               onClick={HandleLogout}
    //               onMouseOver={(e) => {
    //                 e.target.style.border = 'none';
    //                 e.target.style.backgroundColor = 'white';
    //                 e.target.style.border = 'solid 1px black';
    //                 e.target.style.color = 'black';
    //               }}
    //               onMouseOut={(e) => {
    //                 e.target.style.backgroundColor = 'red';
    //                 e.target.style.border = 'none';
    //                 e.target.style.color = 'white';
    //               }}
    //           >
    //             Logout
    //       </Button>
    //
    //         </Link>
    //     </Navbar.Collapse>
    //   </Container>
    // </Navbar>
  );
};

export default NavBar;
