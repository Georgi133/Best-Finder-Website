import { useState } from "react";
import { MyNavBar } from "../Header/MyNavBar";
import style from "./TorrentNotExist.module.css";
import { useNavigate } from "react-router-dom";

export const TorrentNotExist = () => {
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
    <div className={style.container}>
      <MyNavBar />
      {reloadPage && reload()}
      <h1 className={style.header}>Torrent does not exist!</h1>
    </div>
  );
};
