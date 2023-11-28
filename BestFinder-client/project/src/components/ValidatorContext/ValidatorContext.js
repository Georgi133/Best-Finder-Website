import { createContext, useContext, useState } from "react";
import { useAuthContext } from "../AuthContext/AuthContext";

export const ValidatorContext = createContext();

export const ValidatorProvider = ({ children }) => {
  const { setErrorMessage, errorMessage } = useAuthContext();

  const [valid, setValid] = useState(false);

  const onSearchInTorrentValidator = () => {
    setValid(true);
  }

  const validateLogin = (values) => {
    const errors = emailAndPasswordValidation(values);

    setValid(Object.keys(errors).length === 0);
    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
    return errors;
  };

  const validateChat = (message) => {
    let errors = {};
    if(!message) {
      errors.message = "Must be at least 1 character!"
    }
    console.log(errors.message)
    return errors;
  }


  const torrentNameAndFileValidate = (values) => {

    const errors = {};

    if(!values.file) {
      errors.file = "File must be added"
    }

    if (!values.torrentName) {
      errors.torrentName = "Name is required!";
    } else if (values.torrentName.length < 4) {
      errors.torrentName = "Name must be equal or more than 4 characters!";
    }
    return errors;
  };

  const torrentResumeValidate = (errors, values) => {

    if (!values.torrentResume) {
      errors.torrentResume = "Resume is required!";
    } else if (values.torrentResume.length < 4) {
      errors.torrentResume = "Resume must be equal or more than 4 characters!";
    }
    return errors;
  };

  const serialValidate = (values) => {
    let errors = torrentNameAndFileValidate(values);
    errors = torrentResumeValidate(errors,values);

    const isSeason = Number(values.seasons) + 5;

    if (Number(values.seasons) < 1 || isNaN(isSeason)) {
      errors.seasons = "Seasons must be valid positive number";
    } else if (Number(values.seasons) > 20) {
      errors.seasons = "Seasons cannot be more than 20";
    }

    errors = trailerValidate(errors, values);
    errors = actorValidate(errors, values);
    errors = validateCategory(errors, values);

    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
    return errors;
  }

  const gameValidate = (values) => {
    let errors = torrentNameAndFileValidate(values);
    errors = torrentResumeValidate(errors,values);
    const isYearNumber = Number(values.releasedYear) + 5;

    if (Number(values.releasedYear) < 1900 || isNaN(isYearNumber)) {
      errors.releasedYear = "Year must be more or equal to 1900!";
    }
    errors = trailerValidate(errors, values);
    errors = validateCategory(errors, values);

    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
    return errors;
  };

  const jokeValidate = (values) => {
    let errors = torrentNameAndFileValidate(values);
    if (!values.text) {
      errors.text = "Resume is required!";
    } else if (values.text.length < 15) {
      errors.text = "Resume must be equal or more than 15 characters!";
    }
    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
    return errors;
  }

 const validateComment = (values) => {
  console.log(values)
    let errors = {};
    if(!values.comment) {
      errors.comment = "Comment must contain at least 1 character!";
    }
    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
    setValid(Object.keys(errors).length === 0);
    console.log(errors)
    return errors;
  }

  const songValidate = (values) => {
    let errors = torrentNameAndFileValidate(values);
    const isYearNumber = Number(values.releasedYear) + 5;

    if (Number(values.releasedYear) < 1900 || isNaN(isYearNumber)) {
      errors.releasedYear = "Year must be more or equal to 1900!";
    }

     if(!values.songVideo) {
      errors.songVideo = "Cannot be empty!"
    }else  if(!values.songVideo.includes('www.youtube.com')) {
      errors.songVideo = "Must be song from youtube!";
    } else if(values.songVideo.length < 4) {
      errors.songVideo = "Must be more or equal to 4 characters!"
    }

    if(!values.singer1) {
      errors.singer = "Must have at least 1 actor"
    }else if(values.singer1.length < 4) {
      errors.singer = "Must be more or equal to 4 characters!"
    }

    errors = validateCategory(errors, values);

    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
    return errors;

  }

  const movieValidate = (values) => {
    let errors = torrentNameAndFileValidate(values);
    errors = torrentResumeValidate(errors,values);

    const isYearNumber = Number(values.releasedYear) + 5;

    if (Number(values.releasedYear) < 1900 || isNaN(isYearNumber)) {
      errors.releasedYear = "Year must be more or equal to 1900!";
    }

    errors = trailerValidate(errors, values);
    errors = actorValidate(errors, values);
    errors = validateCategory(errors, values);

    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
    return errors;
  }


  const trailerValidate = (errors, values) => {
 if(!values.trailer) {
      errors.trailer = "Cannot be empty!"
    }else  if(!values.trailer.includes('www.youtube.com')) {
      errors.trailer = "Must be trailer from youtube!";
    } else if(values.trailer.length < 4) {
      errors.trailer = "Must be more or equal to 4 characters!"
    }
    return errors;
  }

  const actorValidate = (errors, values) => {
    if(!values.actor1) {
      errors.actor = "Must have at least 1 actor"
    }else if(values.actor1.length < 4) {
      errors.actor = "Must be more or equal to 4 characters!"
    }
    return errors;
  }

  const validateCategory = (errors, values) => {
    if(!values.category1) {
      errors.category = "Must have at least 1 category"
    }
    return errors;
  }


  const changeRoleValidateEmail = (values) => {
    const errors = {};
    const regex = /[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$/i;


    if (!values.email) {
      errors.email = "Email is required!";
    } else if (!regex.test(values.email)) {
      errors.email = "This is not a valid email format!";
    }

    setValid(Object.keys(errors).length === 0);
    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
    return errors;

  }


  const onPasswordRegeneration = (values) => {
    const errors = {};
    const regex = /[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$/i;


    if (!values.userEmail) {
      errors.userEmail = "Email is required!";
    } else if (!regex.test(values.userEmail)) {
      errors.userEmail = "This is not a valid email format!";
    }

    setValid(Object.keys(errors).length === 0);
    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
    return errors;

  }

  const validateEditProfile = (values) => {
    const errors = {};

    if (values.fullName.length < 4) {
      errors.fullName =
        "First and Last name must be equal or more than 4 characters!";
    }
    const isAgeNumber = Number(values.age) + 5;

    if (Number(values.age) < 1 || isNaN(isAgeNumber)) {
      errors.age = "Invalid age!";
    }

    setValid(Object.keys(errors).length === 0);
    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
    return errors;

  }

  const validateChangePassword = (values) => {
    const errors = {};
    if (values.newPassword !== values.confirmPassword) {
      errors.password = "Passwords do not match!";
    } else {
      if (!values.newPassword) {
        errors.password = "Password is required!";
      } else if (values.newPassword.length < 4) {
        errors.password = "Password must be equal or more than 4 characters!";
      } else if (values.newPassword.length > 20) {
        errors.password = "Password cannot be more than 20 characters!";
      }
      if (!values.currentPassword) {
        errors.currentPassword = "Password is required!";
      } else if (values.currentPassword.length < 4) {
        errors.currentPassword = "Password must be equal or more than 4 characters!";
      } else if (values.currentPassword.length > 20) {
        errors.currentPassword = "Password cannot be more than 20 characters!";
      }
    }
    setValid(Object.keys(errors).length === 0);
    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
    return errors;
  };

  const validateRegister = (values) => {
    const errors = emailAndPasswordValidation(values);

    if (values.password !== values.confirmPassword) {
      errors.password = "Passwords do not match!";
    }

    if (values.fullName.length < 4) {
      errors.fullName =
        "First and Last name must be equal or more than 4 characters!";
    }

    const isAgeNumber = Number(values.age) + 5;

    if (Number(values.age) < 1 || isNaN(isAgeNumber)) {
      errors.age = "Invalid age!";
    }

    setValid(Object.keys(errors).length === 0);
    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
    return errors;
  };



  const emailAndPasswordValidation = (values) => {
    const errors = {};

    const regex = /[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$/i;

    if (!values.email) {
      errors.email = "Email is required!";
    } else if (!regex.test(values.email)) {
      errors.email = "This is not a valid email format!";
    }

    if (!values.password) {
      errors.password = "Password is required!";
    } else if (values.password.length < 4) {
      errors.password = "Password must be equal or more than 4 characters!";
    } else if (values.password.length > 20) {
      errors.password = "Password cannot be more than 20 characters!";
    }
    return errors;
  };

  const validatorValues = {
    validateRegister,
    validateLogin,
    setValid,
    validateChangePassword,
    validateEditProfile,
    changeRoleValidateEmail,
    onPasswordRegeneration,
    movieValidate,
    serialValidate,
    gameValidate,
    songValidate,
    jokeValidate,
    validateComment,
    onSearchInTorrentValidator,
    validateChat,
    valid,
  };

  return (
    <ValidatorContext.Provider value={validatorValues}>
      {children}
    </ValidatorContext.Provider>
  );
};

export const useValidatorContext = () => {
  const context = useContext(ValidatorContext);

  return context;
};
