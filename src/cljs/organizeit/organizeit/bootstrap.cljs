(ns organizeit.components.bootstrap
  (:require [cljsjs.react-bootstrap]
            [goog.string :as g-string]
            [reagent.core :as r]))

(def button (r/adapt-react-class (.-Button js/ReactBootstrap)))
(def navbar (r/adapt-react-class (.-Navbar js/ReactBootstrap)))
(def navbar-header (r/adapt-react-class (.-Navbar.Header js/ReactBootstrap)))
(def navbar-brand (r/adapt-react-class (.-Navbar.Brand js/ReactBootstrap)))
(def grid (r/adapt-react-class (.-Grid js/ReactBootstrap)))
(def panel (r/adapt-react-class (.-Panel js/ReactBootstrap)))
(def row (r/adapt-react-class (.-Row js/ReactBootstrap)))
(def col (r/adapt-react-class (.-Col js/ReactBootstrap)))
(def table (r/adapt-react-class (.-Table js/ReactBootstrap)))
(def jumbotron (r/adapt-react-class (.-Jumbotron js/ReactBootstrap)))
(def checkbox (r/adapt-react-class (.-Checkbox js/ReactBootstrap)))