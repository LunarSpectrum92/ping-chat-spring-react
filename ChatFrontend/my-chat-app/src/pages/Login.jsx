import React, {useEffect, useState} from "react";
import { motion } from "framer-motion";
import useAuth from "../hooks/useAuth";
import { authPublic } from "../api/auth.js";
import { useNavigate, useLocation, Link } from 'react-router-dom';

const Login = () => {
  const [username, setuserName] = useState("");
  const [password, setPassword] = useState("");
  const [remember, setRemember] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { auth, setAuth } = useAuth();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/";
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");

    if (!username || !password) {
      setError("Please insert name and password");
      return;
    }
    setLoading(true);
    try {
      await logIn(username, password);
      navigate(from, { replace: true });
    } catch (err) {
      setError(err.response?.data?.message || "Login failed, please try again later");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    console.log("Aktualny stan auth:", auth);
  }, [auth]);

  async function logIn(username, password) {
    const loginData = { username, password };
    try {
      const response = await authPublic.post('/login', loginData);
      // const token = typeof response.data === 'string'
      //     ? response.data
      //     : response.data.token;
      const token = response.data.token;
      const AuthId = response.data.AuthId;
      console.log("login", token);
      setAuth({ name: username, accessToken: token, AuthId: AuthId });
      return response.data;
    } catch (err) {
      console.error("Login error:", err);
      throw err;
    }
  }

  return (
      <div className="">
        <div className="flex items-center justify-center p-4 mt-10">
          <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.4 }}
              className="w-full max-w-md"
          >
            <h2 className="text-3xl font-bold text-center mb-6">Log in</h2>

            {error && (
                <div className="alert alert-error shadow-lg mb-6">
                  <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  <span>{error}</span>
                </div>
            )}

            <div className="card bg-base-300 shadow-xl border border-base-300">
              <form onSubmit={handleSubmit} className="card-body">
                <div className="form-control w-full">
                  <label className="label">
                    <span className="label-text font-semibold">Name</span>
                  </label>
                  <input
                      type="text"
                      placeholder="Jan"
                      className="input input-bordered w-full focus:input-primary"
                      value={username}
                      onChange={(e) => setuserName(e.target.value)}
                      required
                  />
                </div>

                <div className="form-control w-full mt-4">
                  <label className="label">
                    <span className="label-text font-semibold">Password</span>
                  </label>
                  <div className="join w-full">
                    <input
                        type={showPassword ? "text" : "password"}
                        placeholder="your password"
                        className="input input-bordered join-item w-full focus:input-primary"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <button
                        type="button"
                        className="btn btn-ghost join-item border border-base-300"
                        onClick={() => setShowPassword(!showPassword)}
                    >
                      {showPassword ? "Hide" : "Show"}
                    </button>
                  </div>
                </div>

                {/* Remember Me & Forgot Password */}
                <div className="flex items-center justify-between mt-6">
                  <div className="form-control">
                    <label className="label cursor-pointer gap-2">
                      <input
                          type="checkbox"
                          className="checkbox checkbox-primary checkbox-sm"
                          checked={remember}
                          onChange={(e) => setRemember(e.target.checked)}
                      />
                      <span className="label-text">Remember me</span>
                    </label>
                  </div>
                  <Link to="#" className="text-sm link link-primary no-underline hover:underline">
                    Forgot password?
                  </Link>
                </div>

                {/* Submit Button */}
                <div className="form-control mt-6">
                  <button
                      className={`btn btn-primary w-full ${loading ? 'loading' : ''}`}
                      disabled={loading}
                  >
                    {loading ? "Loading..." : "Log in"}
                  </button>
                </div>

                <p className="text-center mt-4 text-sm text-base-content/70">
                  Don't have an account?{" "}
                  <Link to="/register" className="link link-primary font-semibold no-underline hover:underline">
                    Register
                  </Link>
                </p>
              </form>
            </div>
          </motion.div>
        </div>
      </div>
  );
};

export default Login;