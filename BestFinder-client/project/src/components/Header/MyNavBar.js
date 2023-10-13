import { Link } from "react-router-dom";
import style from "./MyNavBar.module.css";
import { useLocalStorage } from "../useLocalStorage/useLocalStorage";
import { useTranslation } from "react-i18next";
import { useAuthContext } from "../AuthContext/AuthContext";
import { useNavigate } from "react-router-dom";

export const MyNavBar = ({ url }) => {
  const [lang, setLang] = useLocalStorage("lang", {});
  const navigate = useNavigate();
  const { t, i18n } = useTranslation();
  const { userId, userEmail } = useAuthContext();

  const emailForLogin = () => {
    let email = userEmail;
    if (email) {
      let index = email.indexOf("@");
      return (email = email.slice(0, index));
    }

    return email;
  };

  const onProfilChange = (e) => {
    const value = e.target.value;

    if (value === "pass") {
      navigate("/change-password");
    } else if (value === "prof") {
      navigate("/edit-profile");
    }
  };

  const onChangeLanguage = (e) => {
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
      </div>

      <div className={style.innerContainer}>
        {!userId && url !== "login" && (
          <Link to="/login" className={style.pg}>
            <strong>{t("login")}</strong>
          </Link>
        )}
        {!userId && url !== "register" && (
          <Link to="/register" className={style.pg}>
            <strong>{t("register")}</strong>
          </Link>
        )}

        <select
          value={i18n.language}
          onChange={onChangeLanguage}
          className={style.selectContainer + " " + style.changeOpt}
        >
          <option className={style.opt} value="bg">
            {t("language.bulgarian")}
          </option>
          <option className={style.opt} value="en">
            {t("language.english")}
          </option>
        </select>

        {userId && (
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
      </div>
    </div>
  );
};
