//
// Created by raver119 on 13.10.2017.
//

#ifndef LIBND4J_BOOLEANOP_H
#define LIBND4J_BOOLEANOP_H

#include <graph/Context.h>
#include "OpDescriptor.h"
#include "DeclarableOp.h"

namespace nd4j {
    namespace ops {
        template <typename T>
        class ND4J_EXPORT BooleanOp : public DeclarableOp<T> {
        protected:
            OpDescriptor * _descriptor;
        public:
            BooleanOp(const char *name, int numInputs, bool scalar);
            ~BooleanOp();

            bool evaluate(std::initializer_list<nd4j::NDArray<T> *> args);
            bool evaluate(std::vector<nd4j::NDArray<T> *>& args);
            bool evaluate(nd4j::graph::Context<T>& block);

            Nd4jStatus execute(Context<T>* block) override;

            ShapeList *calculateOutputShape(ShapeList *inputShape, nd4j::graph::Context<T> &block) override;

        protected:
            bool prepareOutputs(Context<T>& block);
            virtual Nd4jStatus validateAndExecute(Context<T> &block) = 0;
        };
    }
}



#endif //LIBND4J_BOOLEANOP_H