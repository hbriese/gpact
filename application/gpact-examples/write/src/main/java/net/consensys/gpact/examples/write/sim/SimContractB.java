/*
 * Copyright 2020 ConsenSys Software Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package net.consensys.gpact.examples.write.sim;

import java.math.BigInteger;
import net.consensys.gpact.examples.write.Bc2ContractB;

public class SimContractB {
  private Bc2ContractB bc2ContractB;

  private BigInteger val;

  public SimContractB(Bc2ContractB contract) {
    this.bc2ContractB = contract;
  }

  public void set(BigInteger val) {
    this.val = val;
  }

  public String getRlpFunctionSignature_Set() {
    return this.bc2ContractB.getRlpFunctionSignature_Set(this.val);
  }
}
