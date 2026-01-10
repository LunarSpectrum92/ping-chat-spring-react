import axios from 'axios';

const BASE_URL = "http://localhost:8080/auth";

export const authPublic = axios.create({
    baseURL: BASE_URL,
    withCredentials: true
})

export const authPrivate = axios.create({
    baseURL: BASE_URL,
    headers: {'Content-Type': 'application/json'},
    withCredentials: true
})