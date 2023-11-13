import { useNavigate } from "react-router-dom";
import { MyNavBar } from "../Header/MyNavBar";
import style from "./NotAllowed.module.css";
import { useState } from "react";

export const NotFound = () => {
  const navigate = useNavigate();
  const [reloadPage, setReloadPage] = useState(false);

  const reload = () => {
    setTimeout(redirect,3000);
  };

  const redirect = () => {
    navigate('/');
    setReloadPage(false);
  }

  useState(() => {
    setReloadPage(true)
  }, []);

  return (
    <>
    <MyNavBar />
    <div className={style.container}>
      {reloadPage && reload()}
      <img className={style.picture}   alt='errorPage' src='https://www.verticalrail.com/wp-content/uploads/2015/05/404-Page-Not-Found.png'/>
      <h1 className={style.header}>Page Not Found!</h1>
      <h3>Oops! We couldn't find the page that you are looking for.</h3>
      <h3>Please check the address and try again.</h3>
    </div>
    </>
  );
};