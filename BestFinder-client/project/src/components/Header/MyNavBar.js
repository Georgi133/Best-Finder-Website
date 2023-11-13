import { Link } from "react-router-dom";
import style from "./MyNavBar.module.css";
import { useLocalStorage } from "../useLocalStorage/useLocalStorage";
import { useTranslation } from "react-i18next";
import { useAuthContext } from "../AuthContext/AuthContext";
import { useNavigate } from "react-router-dom";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";

export const MyNavBar = ({ url }) => {
  const [lang, setLang] = useLocalStorage("lang", {});
  const navigate = useNavigate();
  const { t, i18n } = useTranslation();
  const { userEmail, onLogout, userRole } = useAuthContext();
  const { setIsLangugeChanged,isLangugeChanged } = useTorrentContext();

  const emailForLogin = () => {
    let email = userEmail;
    if (email) {
      let index = email.indexOf("@");
      return (email = email.slice(0, index));
    }

    return email;
  };

  const onChangeAdminMenu = (e) => {
    const value = e.target.value;

    if(value === "add") {
      navigate("/admin/add");
    }else if(value === "role") {
      navigate("/admin/change-role")
    }else if(value === "ban") {
      navigate("/admin/ban-menu");
    }

  } 

  const onProfilChange = (e) => {
    const value = e.target.value;

    if (value === "pass") {
      navigate("/users/change-password");
    } else if (value === "prof") {
      navigate("/users/edit-profile");
    }
  };

  const onChangeLanguage = (e) => {
    setIsLangugeChanged(!isLangugeChanged);
    const lang = e.target.value;
    // setLanguage({ lang });
    setLang({ lang });
    i18n.changeLanguage(lang);
  };

  return (
    
    <div className={style.container}>
      <div className={style.innerContainer}>
        {url !== "home" && (
          <Link to="/" className={style.pg}>
            <strong>{t("home")}</strong>
          </Link>
        )}
        <p className={style.pg2}>
          <strong>{t("title") + ` ${emailForLogin() === undefined ? 'Anonymous' : emailForLogin()}`}</strong>
        </p>
        { userRole === 'ADMIN'  &&
        <select
          value="admin"
          onChange={onChangeAdminMenu}
          className={style.selectContainer + " " + style.changeOpt + " " + style.adminSelect}
        >
          <option hidden value="admin">
              {t("adminNav.admin")}
            </option>
          <option className={style.opt} value="add">
          {t("adminNav.addTorrent")}
          </option>
          <option className={style.opt} value="role">
          {t("adminNav.changeRole")}
          </option>
          <option className={style.opt} value="ban">
          {t("ban.title")}
          </option>
          
        </select>
}

      </div>

      <div className={style.innerContainer}>
        {!userEmail && url !== "login" && (
          <Link to="/users/login" className={style.pg}>
            <strong>{t("login")}</strong>
          </Link>
        )}
        {!userEmail && url !== "register" && (
          <Link to="/users/register" className={style.pg}>
            <strong>{t("register")}</strong>
          </Link>
        )}

        <select
          value={i18n.language}
          onChange={onChangeLanguage}
          className={style.selectContainer + " " + style.changeOpt + " " + style.changeOptLang}
        >
          <option className={style.opt} value="bg">
            {t("language.bulgarian")}
          </option>
          <option className={style.opt} value="en">
            {t("language.english")}
          </option>
        </select>

        {userEmail && (
          <select
            value="pr"
            onChange={onProfilChange}
            className={style.selectContainer + " " + style.changeOpt}
          >
            <option hidden value="pr">
              {t("profileChange.profile")}
            </option>
            <option className={style.changeOpt2} value="pass">
              {t("profileChange.changePass")}
            </option>
            <option className={style.changeOpt2} value="prof">
              {t("profileChange.changeProfile")}
            </option>
          </select>
        )}
        {userEmail && (
          <div onClick={onLogout} className={style.pg + " " + style.changeOpt}>
          <strong className={style.logoutBtn}>{t("logout")}</strong>
        </div>
        )}
      </div>
    </div>
  );
};
