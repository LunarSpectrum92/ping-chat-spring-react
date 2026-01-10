import React, { useState } from "react";
import { motion } from "framer-motion";
import { authPublic } from "../api/auth.js";
import Navbar from "../components/NavBar.jsx";
import { Link } from 'react-router-dom';

const Register = () => {
  const [username, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");

    if (!username || !password || !firstName || !lastName) {
      setError("Please insert nickname, password, first name, and last name");
      return;
    }
    setLoading(true);
    try {
      await RegisterUser({ username, password, firstName, lastName });
      // You might want to navigate to login here: navigate("/login");
    } catch (err) {
      setError(err.response?.data?.message || "Registration failed, please try again later");
    } finally {
      setLoading(false);
    }
  }

  async function RegisterUser({ username, password, firstName, lastName }) {
    try {
      const response = await authPublic.post('/register', { username, password, firstName, lastName });
      return response.data;
    } catch (err) {
      const message = err.response?.data?.message || "Registration failed";
      setError(message);
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
            <h2 className="text-3xl font-bold text-center mb-6">Create Account</h2>

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

                {/* Nickname Field */}
                <div className="form-control w-full">
                  <label className="label">
                    <span className="label-text font-semibold">Nick Name</span>
                  </label>
                  <input
                      type="text"
                      placeholder="JanK123"
                      className="input input-bordered w-full focus:input-primary"
                      value={username}
                      onChange={(e) => setUserName(e.target.value)}
                      required
                  />
                </div>

                {/* First Name Field */}
                <div className="form-control w-full mt-2">
                  <label className="label">
                    <span className="label-text font-semibold">First Name</span>
                  </label>
                  <input
                      type="text"
                      placeholder="Jan"
                      className="input input-bordered w-full focus:input-primary"
                      value={firstName}
                      onChange={(e) => setFirstName(e.target.value)}
                      required
                  />
                </div>

                {/* Last Name Field */}
                <div className="form-control w-full mt-2">
                  <label className="label">
                    <span className="label-text font-semibold">Last Name</span>
                  </label>
                  <input
                      type="text"
                      placeholder="Kowalski"
                      className="input input-bordered w-full focus:input-primary"
                      value={lastName}
                      onChange={(e) => setLastName(e.target.value)}
                      required
                  />
                </div>

                {/* Password Field */}
                <div className="form-control w-full mt-2">
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

                {/* Submit Button */}
                <div className="form-control mt-8">
                  <button
                      className={`btn btn-primary w-full ${loading ? 'loading' : ''}`}
                      disabled={loading}
                  >
                    {loading ? "Creating account..." : "Register"}
                  </button>
                </div>

                {/* Login Link */}
                <p className="text-center mt-4 text-sm text-base-content/70">
                  Already have an account?{" "}
                  <Link to="/login" className="link link-primary font-semibold no-underline hover:underline">
                    Log in
                  </Link>
                </p>
              </form>
            </div>
          </motion.div>
        </div>
      </div>
  );
};

export default Register;