// import React from 'react';
import React, {Component} from 'react';

import { MapTo } from '@adobe/aem-react-editable-components';

export const CustomEditConfig ={
  emptyLabel: 'Text Checkbox Component',

  isEmpty: function(props) {
    return !props || !props.text || props.text.trim().length < 1;
  }
};

export default class TextCheckbox extends Component{

  render() {
    if(CustomEditConfig.isEmpty(this.props)){
      return null;
    }

    const { text, checked, isChecked } = this.props;
    const checkboxState = checked == null ? isChecked : checked;
    const isCheckedValue = checkboxState === true || checkboxState === 'true';

    return(
      <div className='TextCheckboxComponent'>
        <h2>{text}</h2>
        <h4>{isCheckedValue ? 'Checked' : 'Not Checked'}</h4>
      </div>
    )
  }
}

MapTo('June/components/reactcheck')(TextCheckbox, CustomEditConfig);

