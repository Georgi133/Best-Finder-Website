import { MyNavBar } from "../Header/MyNavBar";
import { ButtonSubmit } from "../Login/ButtonSubmit";
import style from "./Register.module.css";
import { useTranslation } from "react-i18next";
import { Footer } from "../Footer/Footer";


export const Register = () => {

  const { t } = useTranslation()


  return (
    <>
      <MyNavBar url={"register"} />
      <div className={style.container}>
        <form className={style.formContainer}>
          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="username">
            {t("userForm.username")}:
            </label>
            <input
              type="text"
              name="username"
              className={style.inp + " form-control"}
              id="username"
              placeholder="Enter username"
              required
              min={3}
            />
          </div>
          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="email">
            {t("userForm.email")}:
            </label>
            <input
              type="email"
              name="email"
              className={style.inp + " form-control"}
              id="email"
              placeholder="Enter email"
              required
            />
          </div>
          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="password">
            {t("userForm.password")}:
            </label>
            <input
              type="password"
              className={style.inp + " form-control"}
              id="password"
              name="password"
              placeholder="Password"
              required
              min={3}
            />
          </div>
          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="confirmPassword">
            {t("userForm.confirmPassword")}:
            </label>
            <input
              type="password"
              className={style.inp + " form-control"}
              id="confirmPassword"
              name="confirmPassword"
              placeholder="confirmPassword"
              required
              min={3}
            />
          </div>
          <div>
            <ButtonSubmit />
          </div>
        </form>
      </div>
    </>
  );
};
