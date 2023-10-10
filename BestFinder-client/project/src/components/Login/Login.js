import { MyNavBar } from "../Header/MyNavBar";
import style from "./Login.module.css";
import { useTranslation } from "react-i18next";
import { useState } from "react";
import { Forgotten } from "./Forgotten";
import { ButtonSubmit } from "./ButtonSubmit";
import { Footer } from "../Footer/Footer";

export const Login = () => {
  const [forgottenPass, setForgottenPass] = useState(false);

  const { t } = useTranslation();

  const onClickForgotten = () => {
    setForgottenPass(true);
  };

  return (
    <>
      <MyNavBar url={"login"} />
      {forgottenPass && <Forgotten setForgottenPass={setForgottenPass} />}
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
              placeholder="Enter email"
              required
              min={3}
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

          <div
            className={
              style.login + " " + style.remember + " form-group" + " form-check"
            }
          >
            <a className={style.forgotten} onClick={onClickForgotten}>
              {t("userForm.forgotten")}?
            </a>
            <div className={style.check_submit}>
              <div className={style.remember_check}>
                <input
                  type="checkbox"
                  className={
                    style.checkbox + " " + style.inp + " form-check-input"
                  }
                  id="remember"
                />
                <label className="form-check-label" htmlFor="remember">
                  {t("userForm.remember")}
                </label>
              </div>
            </div>

            <ButtonSubmit/>
          </div>
        </form>
      </div>
    </>
  );
};
