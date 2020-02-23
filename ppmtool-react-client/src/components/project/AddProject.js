import React, {Component} from 'react';

class  AddProject extends Component{
    render() {
        return(
        <div>
            <h1>Create a Project</h1>
            <hr/>
            <form>
                <div className="form-group">
                    <input type="text" className="form-control form-control-lg" placeholder = "Project Name" name="projectName" />
                </div>
                <div className="form-group">
                    <input type="text" className="form-control form-control-lg" placeholder="Unique Project ID" name="projectIdentifier" disabled/>
                </div>
                <div className="form-group">
                    <textarea className="form-control form-control-lg" placeholder="Project Description" name="description" ></textarea>
                </div>
                <h6>Start Date</h6>
                <div className="form-group">
                    <input type="date" className="form-control form-control-lg" name="start_date" />
                </div>
                <h6>Estimated End Date</h6>
                <div className="form-group">
                    <input type="date" className="form-control form-control-lg" name="end_date" />
                </div>
                <input type="submit" className="btn btn-primary btn-block mt-4" />
            </form>
        </div>


        );
    }
}

export default AddProject