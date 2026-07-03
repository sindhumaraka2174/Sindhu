import React from "react";
import { MapTo } from '@adobe/aem-react-editable-components';

const Header = ({ pathfield, textfield, checkbox, items }) => {
  return (
    <div>
      <h2>Header Component Output</h2>

      <p><b>Path Field:</b> {pathfield}</p>
      <p><b>Name:</b> {textfield}</p>

      <h3>MultiField Data:</h3>
      {items && items.map((item, index) => (
        <div key={index}>
          <b>College Name:</b> {item.text}<br />
          <b>Date of birth:</b> {item.date}<br />
        </div>
      ))}

      <p>
        <b>B.Tech:</b> {checkbox ? "Completed" : "Not Completed"}
      </p>
    </div>
  );
};

MapTo('June/components/header')(Header);

export default Header;