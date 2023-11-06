import { Navigate } from "react-router-dom";
import jwt_decode from "jwt-decode";

export const PrivateRoute = ({ children }) => {
    
     const token = JSON.parse(localStorage.getItem("token"));

     if (token) {
        const decoded = jwt_decode(token);
        const { sub, role, exp } = decoded;
        if (new Date(exp * 1000) < new Date()) {
          token = '';
          localStorage.removeItem("token");
        } 
      }

    return token ? children : <Navigate to={'/users/login'}/>
}