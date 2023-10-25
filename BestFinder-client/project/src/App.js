import { Route, Routes } from "react-router-dom";
import { Home } from "./components/Home/Home";
import { Login } from "./components/Login/Login";
import { Register } from "./components/Register/Register";
import { Footer } from "./components/Footer/Footer";
import { AuthProvider } from "./components/AuthContext/AuthContext";
import { ChangePassword } from "./components/ChangePassword/ChangePassword";
import { EditProfile } from "./components/EditProfile/EditProfile";
import { useEffect, useState } from "react";
import  jwt_decode  from 'jwt-decode'
import { ChangeUserRole } from "./components/ChangeUserRole/ChangeUserRole";
import { AddMenu } from "./components/AddMenu/AddMenu";
import { AdminProvider } from "./components/AdminContext/AdminContext";
 
function App() {

  const [user, setUser] = useState({});

  const token = JSON.parse(localStorage.getItem("token"))

  useEffect(() => {
    if(token) {
      const decoded = jwt_decode(token);
      const {sub,role, exp} = decoded;
      if(new Date(exp * 1000) < new Date()) {
        setUser({})
        localStorage.removeItem('token')
      } else {
      setUser({role: role, email: sub})
      }
    }
  }, [])

  return (
    <AuthProvider userReload={user}>
      <AdminProvider>
    <>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/users/login" element={<Login />} />
        <Route path="/users/register" element={<Register />} />
        <Route path="/users/change-password" element={<ChangePassword />} />
        <Route path='/users/edit-profile' element={<EditProfile />} />
        <Route path='/admin/add' element={<AddMenu />}/>
        <Route path='/admin/change-role' element={<ChangeUserRole/>}/>
      </Routes>
      <Footer />
      </>
      </AdminProvider>
    </AuthProvider>
  );
}

export default App;
