import { Navigate } from "react-router-dom";
import jwt_decode from "jwt-decode";
import { NotAllowed } from "../NotAllowed/NotAllowed";

export const PrivateAdminRoute = ({ children }) => {
    
     let token = JSON.parse(localStorage.getItem("token"));

     if (token) {
        const decoded = jwt_decode(token);
        const { role, exp } = decoded;
        if (new Date(exp * 1000) < new Date()) {
          token = '';
          localStorage.removeItem("token");
        } 
        if(role !== 'ADMIN') {
          console.log('here')
            token = '';
        }
      }

    return token ? children : <NotAllowed/>
}