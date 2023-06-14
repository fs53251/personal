import './App.css';
import React from "react";
import { Route, Routes } from "react-router-dom";
import LoginPageWrapper from './components/wrappers/loginPageWrapper';
import HomePageWrapper from './components/wrappers/homePageWrapper';
import NotFound from './container/notFound/notFound';
import AppHeader from './container/appHeader/appHeader';
import KlubRegisterPageWrapper from './components/wrappers/klubRegisterPageWrapper';
import ProfilePageWrapper from './components/wrappers/profilePageWrapper';
import RegisterPlesWrapper from './components/wrappers/registerPlesWrapper'
import PlesPageWrapper from './components/wrappers/plesPageWrapper';
import AdminPageWrapper from './components/wrappers/adminPageWrapper';
import AdminClientsPageWrapper from './components/wrappers/adminClientsPageWrapper';
import AdminClubsPageWrapper from './components/wrappers/adminClubsPageWrapper';
import AdminDancesPageWrapper from './components/wrappers/adminDancesPageWrapper';
import AdminConfirmClubsPageWrapper from './components/wrappers/adminConfirmClubsPageWrapper';
import ClubsWrapper from './components/wrappers/clubsWrapper';
import InfoWrapper from './components/wrappers/infoWrapper';
import KlubPageWrapper from './components/wrappers/klubPageWrapper.js';
import KlubEventNewWrapper from './components/wrappers/klubEventNewWrapper.js';
import TrenerFormWrapper from './components/wrappers/trenerFormWrapper.js';
import KlubAllEventsWrapper from './components/wrappers/klubAllEventsWrapper';
import KlubAllCoursesWrapper from './components/wrappers/klubAllCoursesWrapper';
import PlesnjakPageWrapper from './components/wrappers/plesnjakPageWrapper';
import TecajPageWrapper from './components/wrappers/tecajPageWrapper';
import LogoutPage from './container/logoutPage/logoutPage';
import KlubCourseNewWrapper from './components/wrappers/klubCourseNewWrapper';
import KlubAllTrainersWrapper from './components/wrappers/klubAllTrainersWrapper';
import KlubConfirmTrainersWrapper from './components/wrappers/klubConfirmTrainersWrapper';
import KlubConfirmTrainerRequestWrapper from './components/wrappers/klubConfirmTrainerRequestWrapper';
import TecajEnrolledWrapper from './components/wrappers/tecajEnrolledWrapper';
import CalendarPageWrapper from './components/wrappers/calendarPageWrapper';

function App() {
  return (
    <div className="App">
      <AppHeader/>
      <Routes>
        <Route
          path={"/"}
          element={<HomePageWrapper/>}
        />
        <Route
          path={"/login"}
          element={<LoginPageWrapper/>}
        />
        <Route
          path={"/logout"}
          element={<LogoutPage/>}
        />
        <Route
          path={"/profile"}
          element={<ProfilePageWrapper/>}
        />
        <Route
          path={"/profile/:username"}
          element={<ProfilePageWrapper/>}
        />
        <Route
          path={"/dance/new"}
          element={<RegisterPlesWrapper/>}
        />
  
        <Route
          path={"/clubs"}
          element={<ClubsWrapper/>}
        />
        <Route
          path={"/club/:username/:clubname"}
          element={<KlubPageWrapper/>}
        />
        <Route
          path={"/club/:username/:clubname/trainers"}
          element={<KlubAllTrainersWrapper/>}
        />
        <Route
          path={"/club/:username/:clubname/become-trainer"}
          element={<TrenerFormWrapper/>}
        />
        <Route
          path={"/club/:username/:clubname/confirm-trainers"}
          element={<KlubConfirmTrainersWrapper/>}
        />
        <Route
          path={"/club/:username/:clubname/confirm-trainer/:username"}
          element={<KlubConfirmTrainerRequestWrapper/>}
        />
        <Route
          path={"/club/event/:id"}
          element={<PlesnjakPageWrapper/>}
        />
        <Route
          path={"/club/course/:id"}
          element={<TecajPageWrapper/>}
        />
        <Route
          path={"/club/course/:id/enrolled"}
          element={<TecajEnrolledWrapper/>}
        />
        <Route
          path={"/club/:username/:clubname/events"}
          element={<KlubAllEventsWrapper/>}
        />
        <Route
          path={"/club/:username/:clubname/courses"}
          element={<KlubAllCoursesWrapper/>}
        />
        <Route
          path={"/club/register"}
          element={<KlubRegisterPageWrapper/>}
        />
        <Route
          path={"/club/event/new"}
          element={<KlubEventNewWrapper/>}
        />

        <Route
          path={"/club/course/new"}
          element={<KlubCourseNewWrapper/>}
        />

        <Route
          path={"/calendar"}
          element={<CalendarPageWrapper/>}
        />

        <Route
          path={"/dance/:name"}
          element={<PlesPageWrapper/>}
        />
        <Route
          path={"/admin"}
          element={<AdminPageWrapper/>}
        />
        <Route 
          path={"/admin/clients"}
          element={<AdminClientsPageWrapper />}
        />
        <Route 
          path={"/admin/clubs"}
          element={<AdminClubsPageWrapper />}
        />
        <Route 
          path={"/admin/dances"}
          element={<AdminDancesPageWrapper />}
        />
        <Route 
          path={"/admin/confirm-clubs"}
          element={<AdminConfirmClubsPageWrapper />}
        />
        <Route 
          path={"/info"}
          element={<InfoWrapper/>}
        />

        <Route
          path={"*"}
          element={<NotFound/>}
        />
      </Routes>
    </div>
  );
}

export default App;
