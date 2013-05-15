/*
Copyright 2009-2010 Igor Polevoy 

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License. 
*/


package org.javalite.activejdbc.associations;

import org.javalite.activejdbc.Association;

/**
 * @author Igor Polevoy
 */
public class Many2ManyAssociation extends Association {

    private String sourceFkName, targetFkName;
    private String join, targetPk;

    /**
     *
     * @param source name of source table in relationship
     * @param target name of target table in relationship
     * @param join name of join table in relationship
     * @param sourceFkName name of a foreign key in the join table pointing to the source table PK.
     * @param targetFkName  name of a foreign key in the join table pointing to the target table PK.
     * @param targetPk name of a PK of a target table
     */
    public Many2ManyAssociation(String source, String target, String join, String sourceFkName, String targetFkName, String targetPk) {
        super(source, target);
        this.targetFkName = targetFkName;
        this.sourceFkName = sourceFkName;
        this.join = join;
        this.targetPk = targetPk;
    }

    public Many2ManyAssociation(String source, String target, String join, String sourceFkName, String targetFkName) {
        this(source, target, join, sourceFkName, targetFkName, "id");
    }

    public String getSourceFkName() {
        return sourceFkName;
    }

    public String getTargetFkName() {
        return targetFkName;
    }

    public String getJoin() {
        return join;
    }

    public String getTargetPk() {
        return targetPk;
    }

    @Override
    public String toString() {
        return new StringBuffer().append(getSource()).append("  >---------<  ").append(getTarget())
                .append(", type: ").append("many-to-many").append(", join: ").append(join).toString();
    }

    public boolean equals(Object other) {

        if(other == null || !other.getClass().equals(getClass())){
            return false;
        }

        Many2ManyAssociation otherAss =(Many2ManyAssociation)other;

        return otherAss.getSource().equalsIgnoreCase(getSource())
                && otherAss.getTarget().equalsIgnoreCase(getTarget())
                && otherAss.getSourceFkName().equalsIgnoreCase(getSourceFkName())
                && otherAss.getTargetFkName().equalsIgnoreCase(getTargetFkName())
                && otherAss.getJoin().equalsIgnoreCase(getJoin());
    }
}
